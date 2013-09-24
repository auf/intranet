package com.savoirfairelinux.liferay.client;

public enum Template {

	AUFNOUVELLE(Structure.AUFNOUVELLE, "AUF-nouvelle", ""),
	AUFEVENEMENT(Structure.AUFEVENEMENT, "AUF-evenement", ""),
	AUFACTUALITE(Structure.AUFEVENEMENT, "AUF-actualite", "");
    
    
    private final Structure structure;
    private final String name;
    private final String description;

    private Template(Structure structure, String name, String description) {
        this.structure = structure;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return name();
    }

    public String getStructureId() {
        return structure.getId();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
