package com.softwareag.app.data;

public enum SubmodelElementPropertyType {
    /* ProductCarbonFootprint */
    PCF_ASSET_REFERENCE("PCFAssetReference"),
    PCF_CALCULATION_METHOD("PCFCalculationMethod"),
    PCFCO2EQ("PCFCO2eq"),
    PCF_REFERENCE_VALUE_FOR_CALCULATION("PCFReferenceValueForCalculation"),
    PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION("PCFQuantityOfMeasureForCalculation"),
    PCF_LIVE_CYCLE_PHASE("PCFLiveCyclePhase"),
    PCF_DESCRIPTION("PCFDescription"),

    /* General */
    STREET("Street"),
    HOUSENUMBER("HouseNumber"),
    ZIPCODE("ZipCode"),
    CITYTOWN("CityTown"),
    COUNTRY("Country"),
    LATITUDE("Latitude"),
    LONGITUDE("Longitude"),

    /* TransportCarbonFootprint */
    TCF_CALCULATION_METHOD("TCFCalculationMethod"),
    TCFCO2EQ("TCFCO2eq"),
    TCF_REFERENCE_VALUE_FOR_CALCULATION("TCFReferenceValueForCalculation"),
    TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION("TCFQuantityOfMeasureForCalculation"),
    TCF_PROCESSES_FOR_GREENHOUSE_GAS_EMISSION_IN_A_TRANSPORT_SERVICE("TCFProcessesForGreenhouseGasEmissionInATransportService"),

    /* Nameplate */
    URI_OF_THE_PRODUCT("URIOfTheProduct"),
    MANUFACTURER_NAME("ManufacturerName"),
    SERIAL_NUMBER("SerialNumber"),
    YEAR_OF_CONSTRUCTION("YearOfConstruction"),
    DATE_OF_MANUFACTURE("DateOfManufacture"),

    /* TechnicalData */

    MANUFACTURER_ORDER_CODE("ManufacturerOrderCode"),
    MANUFACTURER_LOGO("ManufacturerLogo"),
    PRODUCT_IMAGE("ProductImage"),

    /* ReferenceProperty */
    REFERENCE_PROPERTY("ReferenceProperty");



    private String idShort;

    SubmodelElementPropertyType(String idShort) {
        this.idShort = idShort;
    }

    public String getIdShort() {
        return idShort;
    }
}
