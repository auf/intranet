package com.savoirfairelinux.auf.hook.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ref_zoneadministrative")
@NamedQueries({ @NamedQuery(name = "AufRegion.findAll", query = "SELECT r FROM AufRegion r WHERE r.active=1 ORDER BY r.name"), })
public class AufRegion {

	@Id
	@Column(name = "code")
	private String id;

	public String getId() {
		return id;
	}

	@Column(name = "nom")
	private String name;

	@Column(name = "actif")
	private Integer active;

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return (active == 1) ? true : false;
	}

	public String getCode() {
		return id;
	}

}
