package com.example.adittrivedi.startrader;

/**
 * Created by adittrivedi on 01/03/2015.
 */
public class GlossaryDataHolder {

    private String name;
    private String description;

    public GlossaryDataHolder(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
