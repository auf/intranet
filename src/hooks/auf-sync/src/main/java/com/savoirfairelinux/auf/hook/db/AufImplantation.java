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
@Table(name = "ref_implantation")
@NamedQueries({
	@NamedQuery(name = "AufImplantation.findAll", query = "SELECT i FROM AufImplantation i WHERE i.active=1 AND i.status=1 ORDER BY i.name"),
	@NamedQuery(name = "AufImplantation.findAllCities", query = "SELECT DISTINCT(i.city) FROM AufImplantation i WHERE i.active=1 AND i.status=1 ORDER BY i.city"),
	@NamedQuery(name = "AufImplantation.findAllTypes", query = "SELECT DISTINCT(i.type) FROM AufImplantation i WHERE i.active=1 AND i.status=1 ORDER BY i.type"),    
})
public class AufImplantation {
	
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String name;
    @Column(name = "actif")
    private long active;
    @Column(name = "statut")
    private long status;
    @Column(name = "adresse_physique_ville")
    private String city;
    @Column(name = "adresse_physique_pays")
    private String country;
    @Column(name = "type")
    private String type;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "adresse_postale_precision_avant")
    private String adresse_postale_precision_avant;
    @Column(name = "adresse_postale_no")
    private String adresse_postale_no;
    @Column(name = "adresse_postale_rue")
    private String adresse_postale_rue;
    @Column(name = "adresse_postale_bureau")
    private String adresse_postale_bureau;
    @Column(name = "adresse_postale_precision")
    private String adresse_postale_precision;
    @Column(name = "adresse_postale_boite_postale")
    private String adresse_postale_boite_postale;
    @Column(name = "adresse_postale_code_postal_avant_ville")
    private String adresse_postale_code_postal_avant_ville;
    @Column(name = "adresse_postale_code_postal")
    private String adresse_postale_code_postal;
    @Column(name = "adresse_postale_ville")
    private String adresse_postale_ville;
    @Column(name = "adresse_postale_region")
    private String adresse_postale_region;
    

	@ManyToOne
	@JoinColumn(name="region")
    private AufRegion region;

	public AufRegion getRegion() {
		return region;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getFax() {
		return fax;
	}
	
	public long getId() {
		return id;
	}

	public String getPostalAddress() {
		StringBuilder postalAddress = new StringBuilder();
		
		if ((adresse_postale_precision_avant  != null) && (!adresse_postale_precision_avant.equals(""))) {
			postalAddress.append(adresse_postale_precision_avant).append("<br />");
		}
		
		if ((adresse_postale_no  != null) && (!adresse_postale_no.equals(""))) {
			postalAddress.append(adresse_postale_no).append(",");
		}

		if ((adresse_postale_rue  != null) && (!adresse_postale_rue.equals(""))) {
			postalAddress.append(adresse_postale_rue).append("<br />");
		}
		
		if ((adresse_postale_bureau  != null) && (!adresse_postale_bureau.equals(""))) {
			postalAddress.append(adresse_postale_bureau).append(" ");
		}
		
		if ((adresse_postale_precision  != null) && (!adresse_postale_precision.equals(""))) {
			postalAddress.append(adresse_postale_precision).append("<br />");
		}
		
		if ((adresse_postale_boite_postale  != null) && (!adresse_postale_boite_postale.equals(""))) {
			postalAddress.append(adresse_postale_boite_postale).append("<br />");
		}
		
		if ((adresse_postale_code_postal_avant_ville  != null) && (!adresse_postale_code_postal_avant_ville.equals(""))) {
			
			if ((adresse_postale_code_postal  != null) && (!adresse_postale_code_postal.equals(""))) {
				postalAddress.append(adresse_postale_code_postal).append(" ");
			}
			
			if ((adresse_postale_ville  != null) && (!adresse_postale_ville.equals(""))) {
				postalAddress.append(adresse_postale_ville);
			}
			
			if ((adresse_postale_region  != null) && (!adresse_postale_region.equals(""))) {
				postalAddress.append(", ").append(adresse_postale_region).append("<br />");
			}
			
		} else {
			if ((adresse_postale_ville  != null) && (!adresse_postale_ville.equals(""))) {
				postalAddress.append(adresse_postale_ville);
			}
			
			if ((adresse_postale_region  != null) && (!adresse_postale_region.equals(""))) {
				postalAddress.append(", ").append(adresse_postale_region);
			}
			
			if ((adresse_postale_code_postal  != null) && (!adresse_postale_code_postal.equals(""))) {
				postalAddress.append(" ").append(adresse_postale_code_postal);
			}
		}

		return postalAddress.toString();
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}
    
 
}
