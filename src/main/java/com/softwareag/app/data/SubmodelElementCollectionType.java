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

}
