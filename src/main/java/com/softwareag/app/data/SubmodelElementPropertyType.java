package com.softwareag.app.data;

public enum SubmodelElementPropertyType {
    PCF_CALCULATION_METHOD("PCFCalculationMethod"),
    PCFCO2EQ("PCFCO2eq"),
    PCF_REFERENCE_VALUE_FOR_CALCULATION("PCFReferenceValueForCalculation"),
    PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION("PCFQuantityOfMeasureForCalculation"),
    PCF_LIVE_CYCLE_PHASE("PCFLiveCyclePhase"),
    STREET("Street"),
    HOUSENUMBER("HouseNumber"),
    ZIPCODE("ZipCode"),
    CITYTOWN("CityTown"),
    COUNTRY("Country"),
    LATITUDE("Latitude"),
    LONGITUDE("Longitude"),
    TCF_CALCULATION_METHOD("TCFCalculationMethod"),
    TCFCO2EQ("TCFCO2eq"),
    TCF_REFERENCE_VALUE_FOR_CALCULATION("TCFReferenceValueForCalculation"),
    TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION("TCFQuantityOfMeasureForCalculation"),
    TCF_PROCESSES_FOR_GREENHOUSE_GAS_EMISSION_IN_A_TRANSPORT_SERVICE("TCFProcessesForGreenhouseGasEmissionInATransportService");

    private String idShort;

    SubmodelElementPropertyType(String idShort) {
        this.idShort = idShort;
    }

    public String getIdShort() {
        return idShort;
    }
}
