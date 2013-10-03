package com.savoirfairelinux.auf.hook.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ref_service")
@NamedQueries({
    @NamedQuery(name = "AufService.findAll", query = "SELECT s FROM AufService s"),
})

public class AufService {
	
	@Id
    private Long id;
    
    @Column(name = "nom")
    private String name;

	public String getName() {
		return name;
	}

}
