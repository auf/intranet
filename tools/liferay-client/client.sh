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
		"$0" categories
		"$0" structures
		"$0" templates
		;;
	*)
		echo "$0 [categories|structures|templates|all]"
		;;
esac
