#coding: utf-8

import os.path as op

from fabric.api import env, task, sudo, run, cd, lcd, local

env.roledefs['vagrant'] = ["vagrant@10.0.0.17"]
env.roles = ['vagrant']

role = env.roles[0]

if role == 'vagrant':
    if not op.exists(op.expanduser('~/.ssh/config')):
        print "Votre fichier ~/.ssh/config n'existe pas et doit exister pour que la prochaine commande fonctionne. Creation d'une config lambda."
        with open(op.expanduser('~/.ssh/config'), 'wt') as fp:
            fp.write("IdentityFile ~/.ssh/id_rsa")
    local('ssh-add %s' % op.expanduser('~/.vagrant.d/insecure_private_key'))

def mount():
    local("sudo mount -O soft,timeo=5,retrans=5,actimeo=10,retry=5 -o nolock 10.0.0.17:/opt/liferay-portal mount/")

def unmount():
    local("sudo umount ./mount")

def catalina_out():
    run("tail -f /opt/liferay-portal/tomcat/logs/catalina.out")

def mvn_deploy_all():
    with lcd("../../src"):
        local("mvn liferay:deploy")

def mvn_deploy_theme():
    with lcd("../../src/themes/auf-theme"):
        local("mvn liferay:deploy")

def lfr_restart():
    run("/etc/init.d/liferay stop")
    run("/etc/init.d/liferay start")
