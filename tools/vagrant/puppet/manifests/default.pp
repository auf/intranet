class auf {
	$auf_data=extlookup("auf_data")

  # create database
	exec { "auf-db-create":
		require	=> Service["mysql"],
		command	=> "mysql -uroot -e 'create database auf'",
		unless	=> "mysql -uroot auf"
	}

	# Init database from a dump sql
	exec { "auf-db-init":
		require	=> Exec["liferay-db-create"],
		command	=> "mysql -uroot auf < /vagrant/${auf_data}",
        onlyif  => "test -f /vagrant/${auf_data}" #,
        #unless  => "mysql -uroot auf -e 'desc ref_employe'"
	}
}

class liferay {
	$liferay_folder="/opt/liferay-portal"
	$liferay_version=extlookup("liferay_version")
	$liferay_archive=extlookup("liferay_archive")
	$database_backup=extlookup("database_backup")

	file { ["/vagrant/mount","${liferay_folder}"]:
		ensure	=> directory,
		owner	=> vagrant,
		group	=> vagrant
	}
	
	# ZIP file deployment (two steps), first unzip in /tmp then move data to /opt
	exec { "unzip":
		require	=> [File["${liferay_folder}"], Class["unzip"]],
		command => "unzip -qq /vagrant/${liferay_archive} -d /tmp/",
		creates	=> "/tmp/liferay-portal-${liferay_version}/data",
		unless	=> "test -d ${liferay_folder}/data",
		user	=> vagrant,
		timeout => 0
	}
	exec { "deploy":
		require => Exec["unzip"],
		command	=> "mv /tmp/liferay-portal-${liferay_version}/* ${liferay_folder}/",
		creates	=> "${liferay_folder}/data",
		user	=> vagrant
	}

	# symlink tomcat-VERSION to tomcat, make init script easier to maintain
	exec { "tomcat-symlink":
		require	=> Exec["deploy"],
		command	=> "ln -s ${liferay_folder}/tomcat-* ${liferay_folder}/tomcat",
		unless	=> "test -h ${liferay_folder}/tomcat",
		user	=> vagrant
	}
	
	# copy portal-ext.properties
	file { "${liferay_folder}/portal-ext.properties":
		require => Exec["deploy"],
		ensure	=> present,
		source	=> "/vagrant/puppet/templates/portal-ext.properties",
		owner	=> vagrant,
		group	=> vagrant,
		mode	=> 644
	}
	
	# copy context.xml
	file { "${liferay_folder}/tomcat/conf/context.xml":
		require => Exec["deploy"],
		ensure	=> present,
		source	=> "/vagrant/puppet/templates/context.xml",
		owner	=> vagrant,
		group	=> vagrant,
		mode	=> 644
	}

	# create database
	exec { "liferay-db-create":
		require	=> Service["mysql"],
		command	=> "mysql -uroot -e 'create database liferay'",
		unless	=> "mysql -uroot liferay"
	}

	# Init database from a dump sql
	exec { "liferay-db-init":
		require	=> Exec["liferay-db-create"],
		command	=> "mysql -uroot liferay < /vagrant/${database_backup}",
        onlyif  => "test -f /vagrant/${database_backup}",
        unless  => "mysql -uroot liferay -e 'desc User_'"
	}
	
	# install init script
	file { "/etc/init.d/liferay":
		ensure	=> present,
		source	=> "/vagrant/puppet/templates/liferay-init",
		owner	=> root,
		group	=> root,
		mode	=> 755
	}
	
	# add liferay service to startup and start it
	service { "liferay":
		require	=> [File["/etc/init.d/liferay"], Exec["tomcat-symlink"], File["$liferay_folder/portal-ext.properties"], Exec["liferay-db-init"]],
		enable	=> true,
		hasstatus => true,
		ensure  => running
	}
	
    exec { "share-liferay":
        command => "echo \"${liferay_folder}/ 10.0.0.1/24(rw,no_subtree_check,all_squash,anonuid=1000,anongid=1000)\" > /etc/exports",
        require => Exec["deploy"],
        notify => Class["nfs"]
    }
}

class apt-get-update {
	file { "/etc/apt/sources.list.d/webupd8team-java-precise.list":
		ensure	=> present,
		source	=> "/vagrant/puppet/templates/webupd8team-java-precise.list",
		owner	=> root,
		group	=> root,
		mode	=> 0644
  	}
	exec { "add-ppa-key":
		command	=> "apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886",
		require => File["/etc/apt/sources.list.d/webupd8team-java-precise.list"],
		unless	=> "apt-key list | grep EEA14886"
	}
	
	exec { "apt-get update":
		command => "apt-get update",
		require	=> Exec["add-ppa-key"]
  	}
}

class oracle-java7-jdk {
	exec { "accept-jdk-license":
		command	=> "echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections",
		unless	=> "debconf-show oracle-java7-installer | grep 'shared/accepted-oracle-license-v1-1: true'"
	}

	package { "oracle-java7-installer":
		ensure	=> present,
		require	=> [Exec["accept-jdk-license"], Class["apt-get-update"]]
	}
}

class oracle-java6-installer {
    exec { "accept-jdk-license":
        command => "echo oracle-java6-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections",
        unless  => "debconf-show oracle-java6-installer | grep 'shared/accepted-oracle-license-v1-1: true'"
    }
    exec { "accept-jre-license":
        command => "echo oracle-java6-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections",
        unless  => "debconf-show oracle-java6-installer | grep 'shared/accepted-oracle-license-v1-1: true'"
    }
    package { "oracle-java6-installer":
        ensure  => present,
        require => [Exec["accept-jdk-license"], Exec["accept-jre-license"], Class["apt-get-update"]]
    }
}

class unzip {
	package { "unzip":
    ensure	=> present,
    require => Class["apt-get-update"]
  }
}

class mysql {
    exec { "root-passwd":
        command => "echo mysql-server mysql-server/root_password select '' | debconf-set-selections",
        unless  => "debconf-show mysql-server | grep mysql-server/root_password"
    }
    exec { "root-passwd-confirm":
        command => "echo mysql-server mysql-server/root_password_again select '' | debconf-set-selections",
        unless  => "debconf-show mysql-server | grep mysql-server/root_password_again"
    }
    package { "mysql-server":
        ensure  => present,
        require => [Exec["root-passwd"], Exec["root-passwd-confirm"], Class["apt-get-update"]]
    }
    service { "mysql":
        require => Package["mysql-server"],
        enable  => true,
        ensure  => running
    }   
}

class nfs {
    package { "nfs-kernel-server":
        ensure  => present,
        require => Class["apt-get-update"]
    }
    service { "nfs-kernel-server":
        require => Package["nfs-kernel-server"],
        enable  => true,
        ensure  => running
    }   
}

# set exec path once and for all
Exec { path => "/usr/bin:/usr/sbin:/bin:/sbin:/usr/local/bin" }

# extlookup to manage configuration
$extlookup_datadir    = "/vagrant/puppet/configuration"
$extlookup_precedence = ["%{hostname}", "default"]


include apt-get-update
include unzip
#include oracle-java7-jdk
include oracle-java6-installer
include mysql
include nfs
include liferay
include auf
