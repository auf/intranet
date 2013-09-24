package com.savoirfairelinux.liferay.client;

public enum Structure {

	AUFACTUALITE("AUF-actualite", ""),
    AUFEVENEMENT("AUF-evenement", "");
    
    private final String name;
    private final String description;

    private Structure(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return name();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
