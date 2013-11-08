vagrant-liferay
===============

Some vagrant/liferays labs.

## Instructions
* [Install Vagrant](http://docs.vagrantup.com/v1/docs/getting-started/index.html)
* Download the Liferay/Tomcat bundle
* Put the archive in this directory
* Use the configuration templates to create a common.properties corresponding to your Liferay version (in puppet/configuration/)
* Start Vagrant: vagrant up

if needed you can mount the /opt/liferay filesystem:

    mount -O soft,timeo=5,retrans=5,actimeo=10,retry=5 -o nolock 10.0.0.x:/opt/liferay-portal mount/

## PermGen Space error

$ vi /opt/liferay-portal/tomcat/bin/setenv.bat

Modifier le paramètre *-XX:MaxPermSize=256m* pour *-XX:MaxPermSize=512m*
