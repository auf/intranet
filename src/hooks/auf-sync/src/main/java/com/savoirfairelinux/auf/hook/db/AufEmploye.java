package com.savoirfairelinux.auf.hook.db;


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
    @NamedQuery(name = "AufEmploye.findAll", query = "SELECT e FROM AufEmploye e"),
    @NamedQuery(name = "AufEmploye.findLikeName", query = "SELECT e FROM AufEmploye e WHERE LOWER(e.firstName) LIKE :param"),
    @NamedQuery(name = "AufEmploye.findByEmail", query = "SELECT e FROM AufEmploye e WHERE LOWER(e.email) = :email"),
})
public class AufEmploye {
	
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String lastName;
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

	public String getGender() {
		if (gender.equals("m")) return "male";
		return "female";
	}

	public String getPostDesc() {
		return postDesc;
	}

	public String getTelPost() {
		return telPost;
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

}
