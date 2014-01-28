vagrant-liferay
===============

Some vagrant/liferays labs.

## Instructions

### 1. [Install Vagrant](http://docs.vagrantup.com/v1/docs/getting-started/index.html)

### 2. Download the Liferay/Tomcat bundle

### 3. Put the archive in this directory

### 4. Use the configuration templates to create a common.properties corresponding to your Liferay version (in puppet/configuration/)

### 5. Install the following python modules: fabric, watchdog

    $ sudo pip install -r requirements-freeze.txt

### 6. Use fabric to start the VM

    $ fab up

## What's in the fabfile.py ?

See fabric as a commandline proxy.

### Examples

#### Tailing the catalina.out file

Normally, we'd do:

    $ vagrant ssh
    $ tail -f /opt/liferay-portal/tomcat/logs/catalina.out

With fabric, we do:

    $ fab catalina

#### Deploying our theme, hooks, whatever into the VM

Normally, we'd do:

    $ cd ../../src/themes/our-theme
    $ mvn clean package liferay:deploy

With fabric, we do:

    $ fab mvn:theme

We can also deploy everything with the following:

    $ fab mvn

#### Using Sass/Compass

Normally, we'do:

    $ cd ../../src/themes/our-theme/src/main/webapp
    $ compass watch

With fabric, we do:

    $ fab watch

`fab watch` also checks for changes in *javacript* & *velocity* files.

#### Hint

Type the following to see all available commands:

    $ fab ls

## PermGen Space error

$ vi /opt/liferay-portal/tomcat/bin/setenv.sh

Modifier le paramètre *-XX:MaxPermSize=256m* pour *-XX:MaxPermSize=512m*
