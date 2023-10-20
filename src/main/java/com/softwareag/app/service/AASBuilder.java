package com.softwareag.app.service;

import java.util.Arrays;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAdministrativeInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultDataSpecificationIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringDefinitionTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringPreferredNameTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringShortNameTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultQualifier;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultResource;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSpecificAssetID;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;

import com.softwareag.app.utils.AASSimple;

public class AASBuilder {

    private static final String GLOBAL_ASSET_ID = "HTTP://SOFTWARE.AG.COM/351SDSG31SG3541S";

    private static final String SUBMODEL_CARBON_FOOTPRINT_ID = "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0";
    private static final String SUBMODEL_CARBON_FOOTPRINT_SEMANTIC_ID = "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0";
    private static final String SUBMODEL_CARBON_FOOTPRINT_ID_SHORT = "CarbonFootprint";

    private static final String SMC_PRODUCT_CARBON_FOOTPRINT_ID_SHORT = "ProductCarbonFootprint";
    private static final String SMC_PRODUCT_CARBON_FOOTPRINT_SEMANTIC_ID = "https://admin-shell.io/idta/CarbonFootprint/ProductCarbonFootprint/1/0";
    
    private static final String SUBMODEL_TECHNICAL_DATA_ID = "https://admin-shell.io/ZVEI/TechnicalData/Submodel/1/2";

    private static final String SUBMODEL_NAMEPLATE_ID = "www.example.com/ids/sm/1225_9020_5022_1974";

    public static AssetAdministrationShell createAAS(String idShort, String id) {
        return new DefaultAssetAdministrationShell.Builder()
                .idShort(idShort)
                .id(id) 
                .assetInformation(new DefaultAssetInformation.Builder()
                        .assetKind(AssetKind.NOT_APPLICABLE)
                        .globalAssetID(GLOBAL_ASSET_ID)    
                        .build())
                .submodels(new DefaultReference.Builder()
                        .keys(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value(SUBMODEL_CARBON_FOOTPRINT_ID)
                                .build())
                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                        .build())
           /*     .submodels(
                        new DefaultReference.Builder()
                                .keys(new DefaultKey.Builder()
                                        .type(KeyTypes.SUBMODEL)
                                        .value(SUBMODEL_TECHNICAL_DATA_ID)
                                        .build())
                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                .build())
                .submodels(
                        new DefaultReference.Builder()
                                .keys(new DefaultKey.Builder()
                                        .type(KeyTypes.SUBMODEL)
                                        .value(SUBMODEL_NAMEPLATE_ID)
                                        .build())
                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                .build()) */
                .build();
    }

    public static Submodel createSubmodelCarbonFootprint() {
        return new DefaultSubmodel.Builder()
                .semanticID(new DefaultReference.Builder()
                        .keys(new DefaultKey.Builder()
                                .type(KeyTypes.GLOBAL_REFERENCE)
                                .value(SUBMODEL_CARBON_FOOTPRINT_SEMANTIC_ID)
                                .build())
                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                        .build())
                .idShort(SUBMODEL_CARBON_FOOTPRINT_ID_SHORT)
                .id(SUBMODEL_CARBON_FOOTPRINT_ID)
                .description(Arrays.asList(
                        new DefaultLangStringTextType.Builder().text("The Submodel provides the means to access the Carbon Footprint of the asset.").language("en").build()
                ))
                //SMC ProductCarbonFootprint
                /*.submodelElements(new DefaultSubmodelElementCollection.Builder()
                        .idShort(SMC_PRODUCT_CARBON_FOOTPRINT_ID_SHORT)
                        .description(Arrays.asList(
                        new DefaultLangStringTextType.Builder().text("Balance of greenhouse gas emissions along the entire life cycle of a product in a defined application and in relation to a defined unit of use").language("en").build()
                        ))
                        .semanticID(new DefaultReference.Builder()
                                .keys(new DefaultKey.Builder()
                                .type(KeyTypes.GLOBAL_REFERENCE)
                                .value(SMC_PRODUCT_CARBON_FOOTPRINT_SEMANTIC_ID)
                                .build())
                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                        .build())
                        .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("ZeroToMany")
                                .build())
                        //PCFCalculationMethod
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG854#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Standard, method for determining the greenhouse gas emissions of a product").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("PCFCalculationMethod")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("OneToMany")
                                .build())
                                .build())
                        //PCFCO2eq
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG855#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Sum of all greenhouse gas emissions of a product according to the quantification requirements of the standard").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("PCFCO2eq")
                                .valueType(DataTypeDefXSD.DOUBLE)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())
                        //PCFReferenceValueForCalculation
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG856#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Quantity unit of the product to which the PCF information on the CO2 footprint refers").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("PCFReferenceValueForCalculation")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())
                        //PCFQuantityOfMeasureForCalculation
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG857#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Quantity of the product to which the PCF information on the CO2 footprint refers").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("PCFQuantityOfMeasureForCalculation")
                                .valueType(DataTypeDefXSD.DOUBLE)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())
                        //PCFLiveCyclePhase
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG858#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Life cycle stages of the product according to the quantification requirements of the standard to which the PCF carbon footprint statement refers").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("PCFLiveCyclePhase")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("OneToMany")
                                .build())
                                .build())
                        //PCFGoodsAddressHandover
                        .value(new DefaultSubmodelElementCollection.Builder()
                                .idShort("PCFGoodsAddressHandover")
                                .description(Arrays.asList(
                                new DefaultLangStringTextType.Builder().text("Indicates the place of hand-over of the goods ").language("en").build()
                                ))
                                .semanticID(new DefaultReference.Builder()
                                    .keys(new DefaultKey.Builder()
                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                    .value("0173-1#02-ABI497#001")
                                    .build())
                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                .build())
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                //Street
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH956#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Street indication of the place of transfer of goods").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("Street")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //HouseNumber
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH957#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Number for identification or differentiation of individual houses of a street").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("HouseNumber")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //ZipCode
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH958#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Zip code of the goods transfer address").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("ZipCode")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //CityTown
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH959#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Indication of the city or town of the transfer of goods").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("CityTown")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //Country
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-AAO259#005")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Country where the product is transmitted").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("Country")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //Latitude
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH960#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Latitude (B), also called geodetic latitude or latitude (Latin latitudo, English latitude, international abbreviation Lat. or LAT), is the northerly or southerly distance of a point on the earth's surface from the equator, given in angular measure in the unit of measurement degrees").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("Latitude")
                                            .valueType(DataTypeDefXSD.DOUBLE)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())
                                //Longitude
                                .value(new DefaultProperty.Builder()
                                    .semanticID(new DefaultReference.Builder()
                                            .keys(new DefaultKey.Builder()
                                                    .type(KeyTypes.GLOBAL_REFERENCE)
                                                    .value("0173-1#02-ABH961#001")
                                                    .build())
                                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                            .build())
                                            .description(Arrays.asList(
                                            new DefaultLangStringTextType.Builder().text("Geographic longitude, also called longitude (Latin longitudo, English longitude, international abbreviation long or LON), describes one of the two coordinates of a location on the earth's surface, namely its position east or west of a defined (arbitrarily determined) north-south line, the prime meridian").language("en").build()
                                            ))
                                            .category("PARAMETER")
                                            .idShort("Longitude")
                                            .valueType(DataTypeDefXSD.DOUBLE)
                                            .value(" ")
                                            .qualifiers(new DefaultQualifier.Builder()
                                            .type("Cardinality")
                                            .valueType(DataTypeDefXSD.STRING)
                                            .value("ZeroToOne")
                                            .build())
                                            .build())

                                .build())
                        .build()) */
                .build();
    }



    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
                .assetAdministrationShells(createAAS("testIdshort", "testId"))
                .submodels(createSubmodelCarbonFootprint())
                .conceptDescriptions(AASSimple.createConceptDescriptionTitle())
                .conceptDescriptions(AASSimple.createConceptDescriptionDigitalFile())
                .conceptDescriptions(AASSimple.createConceptDescriptionMaxRotationSpeed())
                .conceptDescriptions(AASSimple.createConceptDescriptionRotationSpeed())
                .conceptDescriptions(AASSimple.createConceptDescriptionDocument())
                .build();
    }

}
