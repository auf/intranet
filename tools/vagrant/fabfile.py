#coding: utf-8

from platform import system
import os, time, datetime, sys
import os.path as op

from fabric.api import env, task, sudo, run, cd, lcd, local, put
from fabric.colors import green, red

THEME_NAME = "auf-theme"
VM_IP = "10.0.0.17"

env.roledefs['vagrant'] = ["vagrant@%s" % VM_IP]
env.roles = ['vagrant']

role = env.roles[0]

THEME_NAME = "auf-theme"

if role == 'vagrant':
    if not op.exists(op.expanduser('~/.ssh/config')):
        print "Votre fichier ~/.ssh/config n'existe pas et doit exister pour que la prochaine commande fonctionne. Creation d'une config lambda."
        with open(op.expanduser('~/.ssh/config'), 'wt') as fp:
            fp.write("IdentityFile ~/.ssh/id_rsa")
    local('ssh-add %s' % op.expanduser('~/.vagrant.d/insecure_private_key'))

@task
def mount():
    """
    Mount the mount folder
    """

    print(green(u"Mount en cours..."))

    if system() == "Linux":
        local("sudo mount -O soft,timeo=5,retrans=5,actimeo=10,retry=5 -o nolock %s:/opt/liferay-portal mount/" % VM_IP)

    # Mac OS
    if system() == "Darwin":
        local("sudo mount -t nfs -o resvport %s:/opt/liferay-portal/ mount" % VM_IP)

@task
def unmount():
    """
    Unmount the mount folder
    """

    print(green(u"Unmount en cours..."))

    if system() == "Linux":
        try:
            local("sudo umount -fl ./mount")
        except:
            return

    # Mac OS
    if system() == "Darwin":
        local("sudo umount -f mount")

@task
def log(operation=""):
    """
    tail-ing the latest log files
    """
    print(green("You may call it using the catalina parameter to output the catalina file", True))
    print(green("ex: fab log:catalina"))

    if operation == "":
        today = datetime.date.today()
        run("tail -f /opt/liferay-portal/logs/liferay.%s.log" % today.strftime("%Y-%m-%d"))

    if operation == "catalina":
        run("tail -f /opt/liferay-portal/tomcat/logs/catalina.out")

@task
def mvn(what="all"):
    """
    Deploy every hooks and theme from the vagrant folder
    """

    print(green(u"Déploiement maven en cours..."))

    if what == "all":
        with lcd("../../src"):
            local("mvn clean package liferay:deploy")

        print(green("You can choose to only deploy the theme with the following command:", True))
        print(green("'fab mvn:theme'", True))

    if what == "theme":
        with lcd("../../src/themes/%s" % THEME_NAME):
            local("mvn clean package liferay:deploy")

def compass_compile():
    """
    Running compass compile from the vagrant folder
    """
    with lcd("../../src/themes/%s/src/main/webapp" % THEME_NAME):
        local("compass compile")

@task
def copy_assets(folder=None):
    """
    Copies the following folders content into the VM: css, js, images, templates
    """
    compass_compile()

    assets_folders = [
        "css",
        "js",
        "images",
        "templates"
    ]

    if folder in assets_folders:
        put(
            "../../src/themes/%s/src/main/webapp/%s" % (THEME_NAME, folder),
            "/opt/liferay-portal/tomcat/webapps/%s/" % THEME_NAME
        )
    else:
        for f in assets_folders:
            put(
                "../../src/themes/%s/src/main/webapp/%s" % (THEME_NAME, f),
                "/opt/liferay-portal/tomcat/webapps/%s/" % THEME_NAME
            )
        print(green("You may use a parameter in this function to specify a directory", True))
        print(green("'fab copy_assets:css' will only transfer the css folder", True))

@task
def copy_deployables():
    """
    Copies any packages that aren't managed within maven in the VM
    """
    file_types = [
        "lpkg",
        "xml",
        "war"
    ]

    for ft in file_types:
        put("./deployables/*.%s" % ft, "/opt/liferay-portal/deploy")

@task
def deploy():
    """

    vagrant up + Mounts the mount folder, copy all deployable files and deploy every hooks and the theme
    """
    v("up")

@task
def admin():
    """
    Shows the admin credentials
    """
    print(green(u"Ouvrez votre navigateur à l'url suivante: ~/web/guest/sfl-secret-login"))
    local("sflvault show 4125")

@task
def v(cmd):
    """
    Usual vagrant commands ex: fab v:ssh, fab v:suspend
    """
    cmds = ["ssh", "suspend", "resume", "up", "destroy"]

    if cmd in cmds:
        if cmd in ["suspend", "destroy"]:
            unmount()

        print(green(u"Commande vagrant {} en cours d'éxécution".format(cmd)))

        local("vagrant {}".format(cmd))

        if cmd in ["resume", "up"]:
            mount()

            print(green(u"Veuillez rouler les commandes fabric suivantes:"))
            print(green(u"fab copy_deployables et fab mvn"))

    else:
        print(red(u"La commande vagrant {} n'existe pas".format(cmd)))

@task
def watch():
    """
    Reacts to changes of *.scss, *.js, and *.vm files.
    """
    try:
        from watchdog.events import FileSystemEventHandler
        from watchdog.observers import Observer
    except ImportError:
        abort(red('Install Watchdog python package to watch filesystem files.', True))

    EXTS = ['.js', '.scss', '.vm']

    class ChangeHandler(FileSystemEventHandler):
        def __init__(self, *args, **kwargs):
            super(ChangeHandler, self).__init__(*args, **kwargs)
            self.last_collected = datetime.datetime.now()

        def on_any_event(self, event):
            if event.is_directory:
                return

            current_ext = os.path.splitext(event.src_path)[-1].lower()
            current_filename = os.path.basename(event.src_path)

            if not current_filename.startswith(".") and current_ext in EXTS:
                now = datetime.datetime.now()
                if (datetime.datetime.now() - self.last_collected).total_seconds() < 1:
                    return

                if current_ext == ".scss":
                    copy_assets("css")

                if current_ext == ".js":
                    copy_assets("js")

                if current_ext == ".vm":
                    copy_assets("templates")

                sys.stdout.write('\n')
                self.last_collected = now
            print green('\nWatching *.scss, *.js, and *.vm files for changes.\n')


    event_handler = ChangeHandler()
    observer = Observer()
    observer.schedule(
        event_handler,
        os.path.join("../../src/themes", THEME_NAME),
        recursive=True
    )
    observer.start()
    print green('\nWatching *.scss, *.js, and *.vm files for changes.\n')

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
