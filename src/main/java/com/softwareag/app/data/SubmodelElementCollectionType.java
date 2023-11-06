package com.softwareag.app.data;


public enum SubmodelElementCollectionType {
    /* CarbonFootprint */
    PRODUCT_CARBON_FOOTPRINT("ProductCarbonFootprint"),
    PCF_GOODS_ADDRESS_HANDOVER("PCFGoodsAddressHandover"),
    TRANSPORT_CARBON_FOOTPRINT("TransportCarbonFootprint"),
    TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER("TCFGoodsTransportAddressTakeover"),
    TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER("TCFGoodsTransportAddressHandover"),

    /* TechnicalData */
    GENERAL_INFORMATION("GeneralInformation"),
    PRODUCT_CLASSIFICATIONS("ProductClassifications"),
    PRODUCT_CLASSIFICATION_ITEM("ProductClassificationItem"),
    TECHNICAL_PROPERTIES("TechnicalProperties"),
    MAIN_SECTION("MainSection"),
    SUB_SECTION("SubSection"),
    FURTHER_INFORMATION("FurtherInformation"),

    /* Nameplate */
    CONTACT_INFORMATION("ContactInformation"),
    PHONE("Phone"),
    FAX("Fax"),
    EMAIL("Email"),
    IP_COMMUNICATION("IPCommunication{00}"),
    MARKINGS("Markings"),
    MARKING("Marking"),
    EXPLOSION_SAFETIES("ExplosionSafeties"),
    EXPLOSION_SAFETIY("ExplosionSafety"),
    AMBIENT_CONDITIONS("AmbientConditions"),
    PROCESS_CONDITIONS("ProcessConditions"),
    EXTERNAL_ELECTRICAL_CIRCUIT("ExternalElectricalCircuit"),
    SAFETY_RELATED_PROPERTIES_FOR_PASSIVE_BEHAVIOUR("SafetyRelatedPropertiesForPassiveBehaviour"),
    SAFETY_RELATED_PROPERTIES_FOR_ACTIVE_BEHAVIOUR("SafetyRelatedPropertiesForActiveBehaviour"),
    ASSET_SPECIFIC_PROPERTIES("AssetSpecificProperties"),
    GUIDELINE_SPECIFIC_PROPERTIES("GuidelineSpecificProperties{00}");

    private String idShort;

    SubmodelElementCollectionType(String idShort) {
        this.idShort = idShort;
    }

    public String getIdShort() {
        return idShort;
    }
/* 
    public Map<SubmodelElementPropertyType, List<SubmodelElementCollectionType>> getProperties() {
        Map<SubmodelElementPropertyType, List<SubmodelElementCollectionType>>  properties = new HashMap<>();
        switch (this) {
            case PRODUCT_CARBON_FOOTPRINT:
                properties.put(SubmodelElementPropertyType.PCF_CALCULATION_METHOD, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.PCFCO2EQ, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.PCF_REFERENCE_VALUE_FOR_CALCULATION, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT));
                break;
            case TRANSPORT_CARBON_FOOTPRINT:
                properties.put(SubmodelElementPropertyType.TCF_CALCULATION_METHOD, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.TCFCO2EQ, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT));
                properties.put(SubmodelElementPropertyType.TCF_PROCESSES_FOR_GREENHOUSE_GAS_EMISSION_IN_A_TRANSPORT_SERVICE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT));
            case PCF_GOODS_ADDRESS_HANDOVER:
                properties.put(SubmodelElementPropertyType.STREET, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.HOUSENUMBER, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.ZIPCODE, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.CITYTOWN, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.COUNTRY, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.LATITUDE, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.LONGITUDE, Arrays.asList(SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT, SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER));
                break;
            case TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER:
                properties.put(SubmodelElementPropertyType.STREET, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.HOUSENUMBER, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.ZIPCODE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.CITYTOWN, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.COUNTRY, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.LATITUDE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                properties.put(SubmodelElementPropertyType.LONGITUDE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER));
                break;
            case TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER:
                properties.put(SubmodelElementPropertyType.STREET, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.HOUSENUMBER, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.ZIPCODE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.CITYTOWN, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.COUNTRY, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.LATITUDE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                properties.put(SubmodelElementPropertyType.LONGITUDE, Arrays.asList(SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT, SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER));
                break;
        	default:
                throw new IllegalArgumentException("Invalid property for this SubmodelElementCollection!");
        }
        return properties;
    } */

    /*public List<SubmodelElementCollectionType> getSubmodelElementCollections() {
        List<SubmodelElementCollectionType> subModelElementCollections = new ArrayList<>();
        switch (this) {
            case PRODUCT_CARBON_FOOTPRINT:
                subModelElementCollections.add(SubmodelElementCollectionType.PCF_GOODS_ADDRESS_HANDOVER);
                break;
            case TRANSPORT_CARBON_FOOTPRINT:
                subModelElementCollections.add(SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_TAKEOVER);
                subModelElementCollections.add(SubmodelElementCollectionType.TCF_GOODS_TRANSPORT_ADDRESS_HANDOVER);
                break;
            default:
                throw new IllegalArgumentException("Invalid SubmodelElementCollection!");
        }
        return subModelElementCollections;
    } */

    /* public boolean hasSubmodelElemtnCollections() {
        return getSubmodelElementCollections().size()!=0;
    } */

    //Prinzip
    /*public void calculate(String value, SubModel... models) {
        // Jetzt können Sie eine beliebige Anzahl von SubModel-Objekten übergeben
        for (SubModel model : models) {
            // Verarbeiten Sie jedes SubModel
        }
        // Verarbeiten Sie den Wert "value"
    }*/

}
