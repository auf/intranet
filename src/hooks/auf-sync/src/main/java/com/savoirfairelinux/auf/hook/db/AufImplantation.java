package com.savoirfairelinux.auf.hook.db;


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
    @NamedQuery(name = "AufImplantation.findAll", query = "SELECT i FROM AufImplantation i"),
})
public class AufImplantation {
	
    @Id
    private Long id;

	@ManyToOne
	@JoinColumn(name="region")
    private AufRegion region;

	public AufRegion getRegion() {
		return region;
	}
    
 
}
