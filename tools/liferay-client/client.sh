#!/bin/sh

mvn clean package
case "$1" in
	categories)
		mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateCategories
		;;
	structures)
		mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateStructures
		;;
	templates)
		mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateTemplates
		;;
	all)
		#mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateCategories
		mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateStructures
		mvn exec:java -Dexec.mainClass=com.savoirfairelinux.liferay.client.UpdateTemplates
		;;
	*)
		echo "$0 [categories|structures|templates|all]"
		;;
esac
