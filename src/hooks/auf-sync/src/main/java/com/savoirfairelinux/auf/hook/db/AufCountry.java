package com.savoirfairelinux.auf.hook.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ref_pays")
@NamedQueries({
    @NamedQuery(name = "AufCountry.findAll", query = "SELECT c FROM AufCountry c WHERE c.active=1 ORDER BY c.name"),
})
public class AufCountry {
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String name;
    @Column(name = "actif")
    private long active;
    @Column(name = "code")
    private String code;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
}
