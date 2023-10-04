package com.softwareag.app.data;

public enum SubmodelType {
    CARBON_FOOTPRINT("CarbonFootprint"), 
    NAMEPLATE("Nameplate"), 
    TECHNICAL_DATA("TechnicalData");

    private String idShort;

    SubmodelType(String idShort) {
        this.idShort = idShort;
    }

    public String getIdShort() {
        return idShort;
    }
}
