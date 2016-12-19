package com.savoirfairelinux.auf.hook.db;


import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ref_employe")
@NamedQueries({
    @NamedQuery(name = "AufEmploye.findAll", query = "SELECT e FROM AufEmploye e WHERE e.active=1 ORDER BY e.lastName, e.firstName"),
    @NamedQuery(name = "AufEmploye.findLikeName", query = "SELECT e FROM AufEmploye e WHERE LOWER(e.firstName) LIKE :param AND e.active=1 ORDER BY e.lastName, e.firstName"),
    @NamedQuery(name = "AufEmploye.findByEmail", query = "SELECT e FROM AufEmploye e WHERE LOWER(e.email) = :email AND e.active=1 ORDER BY e.lastName, e.firstName"),
    @NamedQuery(name = "AufEmploye.findByEmails", query = "SELECT e FROM AufEmploye e WHERE LOWER(e.email) IN :emails AND e.active=1 ORDER BY e.lastName, e.firstName")
})
public class AufEmploye {
	
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String lastName;
    @Column(name = "actif")
    private long active;
    @Column(name = "prenom")
    private String firstName;
	@Column(name = "courriel")
    private String email;
	@Column(name = "genre")
    private String gender;
	@Column(name = "fonction")
    private String postDesc;
	@Column(name = "telephone_poste")
    private String telPost;
	@Column(name = "telephone_ip")
    private String telIp;
	@Column(name = "telephone_ip_nomade")
    private String telIpNomade;

	@ManyToOne
	@JoinColumn(name="implantation")
    private AufImplantation implantation;
	
	@ManyToOne
	@JoinColumn(name="implantation_physique")
    private AufImplantation physicalImplantation;
	
	@ManyToOne
	@JoinColumn(name="service")
    private AufService service;
    
    public String getLastName(){
    	return lastName.toUpperCase();
    }

    public String getFirstName() {
		return firstName;
	}
    
    public String getFullName() {
    	return getGenderTitle() + " " + getLastName() + " " + getFirstName();
    }
    
    public String getStuff(String text) {
    	return "YES" + text;
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
		String fn = getFirstName();
		if (fn==null) fn="";
		String l = getLogin();
		if (l==null) l="";
		String e = getEmail();
		if (e==null) e="@";
		return "User: " + l + " - " + ln + " " + fn + " - " + e;
	}

	public String getGender() {
		if (gender.equals("m")) return "male";
		return "female";
	}
	
	public String getGenderTitle() {
		if (gender.equals("m")) return "M.";
		return "Mme";
	}

	public String getPostDesc() {
		return postDesc;
	}
	
	public String getServiceName() {
		return getService().getName();
	}
	
	public String getImplantationName() {
		return getImplantation().getName();
	}
	
	public String getImplantationCity() {
		return getImplantation().getCity();
	}
	
	public String getImplantationRegionName() {
		return getImplantation().getRegion().getName();
	}

	public String getTelephone() {
		if(telPost.trim().isEmpty()){
			return getPhysicalImplantation().getTelephone();
		} else { 
			return String.format("%s poste %s", getPhysicalImplantation().getTelephone(), telPost);
		}
	}
	
	public String getTelecopier() {
		return getPhysicalImplantation().getFax();
	}
	
	public String getPostalAddress() {
		return getPhysicalImplantation().getPostalAddress();
	}

	public String getTelIp() {
		return telIp;
	}

	public String getTelIpNomade() {
		return telIpNomade;
	}

	public long getId() {
		return id;
	}

	public AufImplantation getImplantation() {
		return implantation;
	}
	
	public AufImplantation getPhysicalImplantation() {
		return physicalImplantation;
	}
	
	public AufService getService() {
		return service;
	}

	public boolean isMale() {
		if (gender.equals("m")) return true;
		return false;
	}

	public Date getBirthday() {
		// TODO Auto-generated method stub
		return Calendar.getInstance().getTime();
	}


}
