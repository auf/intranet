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

if role == 'vagrant':
    if not op.exists(op.expanduser('~/.ssh/config')):
        print "Votre fichier ~/.ssh/config n'existe pas et doit exister pour que la prochaine commande fonctionne. Creation d'une config lambda."
        with open(op.expanduser('~/.ssh/config'), 'wt') as fp:
            fp.write("IdentityFile ~/.ssh/id_rsa")
    local('ssh-add %s' % op.expanduser('~/.vagrant.d/insecure_private_key'))

def mount():
    """
    Mount the mount folder
    """

    if system() == "Linux":
        local("sudo mount -O soft,timeo=5,retrans=5,actimeo=10,retry=5 -o nolock %s:/opt/liferay-portal mount/" % VM_IP)

    # [TODO] à faire pour un Mac

def unmount():
    """
    Unmount the mount folder
    """

    if system() == "Linux":
        local("sudo umount -fl ./mount")

    # [TODO] à faire pour un Mac

@task
def catalina():
    """
    tail-ing the catalina.out
    """
    run("tail -f /opt/liferay-portal/tomcat/logs/catalina.out")

@task
def mvn(what="all"):
    """
    Deploy every hooks and theme from the vagrant folder
    """
    if what == "all":
        with lcd("../../src"):
            local("mvn clean package liferay:deploy")

        print(green("You can choose to only deploy the theme with the following command:", True))
        print(green("'mvn deploy:theme'", True))

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
def start():
    """
    Starts Liferay
    """
    sudo("/etc/init.d/liferay start")

@task
def stop():
    """
    Stops Liferay
    """
    sudo("/etc/init.d/liferay stop")

@task
def restart():
    """
    Restarts Liferay
    """
    stop()
    start()

def deploy():
    """
    Mounts the mount folder, copy all deployable files and
    deploy every hooks and the theme
    """
    mount()
    copy_deployables()
    deploy()

@task
def suspend():
    """
    Unmount the mount folder and suspend the VM
    """
    unmount()
    local("vagrant suspend")

@task
def resume():
    """
    Resume the VM & mount the mount folder
    """
    local("vagrant resume")
    mount()

@task
def up():
    """
    Setup the VM and deploy everything
    """
    local("vagrant up")
    deploy()

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
            if current_ext in EXTS:
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
                self.last_collected = datetime.datetime.now()

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