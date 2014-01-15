#coding: utf-8

from platform import system
import os.path as op

from fabric.api import env, task, sudo, run, cd, lcd, local, put

env.roledefs['vagrant'] = ["vagrant@10.0.0.17"]
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

    if system() == "Linux":
        local("sudo mount -O soft,timeo=5,retrans=5,actimeo=10,retry=5 -o nolock 10.0.0.17:/opt/liferay-portal mount/")

    # [TODO] à faire pour un Mac


@task
def unmount():
    """
    Unmount the mount folder
    """

    if system() == "Linux":
        local("sudo umount -fl ./mount")

    # [TODO] à faire pour un Mac

@task
def catalina_out():
    """
    tail-ing the catalina.out file from the vagrant folder
    """
    run("tail -f /opt/liferay-portal/tomcat/logs/catalina.out")

@task
def mvn_deploy_all():
    """
    Deploy every hooks and theme from the vagrant folder
    """
    with lcd("../../src"):
        local("mvn clean package liferay:deploy")

@task
def mvn_deploy_theme():
    """
    Deploy the current theme from the vagrant folder
    """
    with lcd("../../src/themes/%s" % THEME_NAME):
        local("mvn clean package liferay:deploy")

@task
def compass_compile():
    """
    Running compass compile from the vagrant folder
    """
    with lcd("../../src/themes/auf-theme/src/main/webapp"):
        local("compass compile")

@task
def copy_assets(folder=None):
    """
    Copies all CSS and JS files in the VM
    """
    compass_compile()

    if folder:
        put(
            "../../src/themes/auf-theme/src/main/webapp/%s" % folder,
            "/opt/liferay-portal/tomcat/webapps/auf-theme/"
        )
    else:

        assets_folders = [
            "css",
            "js",
            # "images"
        ]

        for f in assets_folders:
            put(
                "../../src/themes/auf-theme/src/main/webapp/%s" % f,
                "/opt/liferay-portal/tomcat/webapps/auf-theme/"
            )

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
def lfr_start():
    """
    Starts Liferay
    """
    sudo("/etc/init.d/liferay start")

@task
def lfr_stop():
    """
    Stops Liferay
    """
    sudo("/etc/init.d/liferay stop")

@task
def lfr_restart():
    """
    Restarts Liferay
    """
    lfr_stop()
    lfr_start()

@task
def deploy():
    """
    Mounts the mount folder, copy all deployable files and
    deploy every hooks and the theme
    """
    mount()
    copy_deployables()
    mvn_deploy_all()


