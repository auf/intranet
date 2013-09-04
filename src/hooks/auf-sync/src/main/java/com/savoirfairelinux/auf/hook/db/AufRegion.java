package com.savoirfairelinux.auf.hook.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ref_region")
@NamedQueries({
    @NamedQuery(name = "AufRegion.findAll", query = "SELECT r FROM AufRegion r"),
})
public class AufRegion {
	
    @Id
    private Long id;
    
    @Column(name = "nom")
    private String name;

	public String getName() {
		return name;
	}

}
