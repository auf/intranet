package com.savoirfairelinux.auf.hook.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ref_employe")
public class AufEmployeTable {
	
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String lastName;
    @Column(name = "prenom")
    private String firstName;
	@Column(name = "courriel")
    private String email;
    
    public String getLastName(){
    	return lastName;
    }

    public String getFirstName() {
		return firstName;
	}

	public String getEmail() {
		return email;
	}
	
	public String getLogin() {
		if (email != null) {
			return email.substring(0, email.indexOf("@"));
		}
		return null;
	}
	
	public String toString() {
		String ln = getLastName();
		if (ln==null) ln="";
		String l = getLogin();
		if (l==null) l="";
		String e = getEmail();
		if (e==null) e="@";
		return "User: " + l + " - " + ln + " - " + e;
	}
}
