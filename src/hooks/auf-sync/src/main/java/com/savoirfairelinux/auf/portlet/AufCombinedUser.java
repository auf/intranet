package com.savoirfairelinux.auf.portlet;

import com.savoirfairelinux.auf.hook.db.AufEmploye;

public class AufCombinedUser {
	
	AufEmploye employe;
	String portraitUrl;
	
	public AufCombinedUser(AufEmploye employe, String portraitUrl) {
		this.employe = employe;
		this.portraitUrl = portraitUrl;
	}

	public AufEmploye getEmploye() {
		return employe;
	}

	public String getPortraitUrl() {
		return portraitUrl;
	}

}
