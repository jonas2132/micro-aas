package com.softwareag.app.service;

import java.util.Arrays;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Blob;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Range;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.builder.AbstractBuilder;
import org.eclipse.digitaltwin.aas4j.v3.model.builder.SubmodelBuilder;
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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder;

import com.softwareag.app.utils.AASSimple;

public class AASBuilder {

    private static final String GLOBAL_ASSET_ID = "HTTP://SOFTWARE.AG.COM/351SDSG31SG3541S";

    private static final String SUBMODEL_CARBON_FOOTPRINT_ID = "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0";
    private static final String SUBMODEL_CARBON_FOOTPRINT_SEMANTIC_ID = "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0";
    private static final String SUBMODEL_CARBON_FOOTPRINT_ID_SHORT = "CarbonFootprint";

    private static final String SMC_PRODUCT_CARBON_FOOTPRINT_ID_SHORT = "ProductCarbonFootprint";
    private static final String SMC_PRODUCT_CARBON_FOOTPRINT_SEMANTIC_ID = "https://admin-shell.io/idta/CarbonFootprint/ProductCarbonFootprint/1/0";
    
    private static final String SMC_TRANSPORT_CARBON_FOOTPRINT_ID_SHORT = "TransportCarbonFootprint";
    private static final String SMC_TRANSPORT_CARBON_FOOTPRINT_SEMANTIC_ID = "https://admin-shell.io/idta/CarbonFootprint/TransportCarbonFootprint/1/0";

    private static final String SUBMODEL_TECHNICAL_DATA_ID = "https://admin-shell.io/ZVEI/TechnicalData/Submodel/1/2";

    private static final String SUBMODEL_NAMEPLATE_ID = "www.example.com/ids/sm/1225_9020_5022_1974";

    public static AssetAdministrationShell copyAAS(Environment environment) {
        org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder builder = new DefaultAssetAdministrationShell.Builder();
        AssetAdministrationShell shell = environment.getAssetAdministrationShells().get(0);
        builder.idShort(shell.getIdShort())
                .displayName(shell.getDisplayName())
                .category(shell.getCategory())
                .description(shell.getDescription())
                .extensions(shell.getExtensions())
                .id(shell.getId()) 
                .administration(shell.getAdministration())
                .embeddedDataSpecifications(shell.getEmbeddedDataSpecifications())
                .derivedFrom(shell.getDerivedFrom())
                .assetInformation(new DefaultAssetInformation.Builder()
                        .assetKind(shell.getAssetInformation().getAssetKind())
                        .globalAssetID(shell.getAssetInformation().getGlobalAssetID())  
                        .assetType(shell.getAssetInformation().getAssetType())
                        .specificAssetIds(shell.getAssetInformation().getSpecificAssetIds())
                        .defaultThumbnail(shell.getAssetInformation().getDefaultThumbnail())  
                        .build());
        environment.getSubmodels().forEach(submodel -> {
                builder.submodels(new DefaultReference.Builder()
                        .keys(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value(submodel.getId())
                                .build())
                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                        .build());
        });
        return builder.build();
    }

    private static Submodel copySubmodel(Submodel submodel) {
        Builder builder = new DefaultSubmodel.Builder();
        
        builder.semanticID(new DefaultReference.Builder()
                        .keys(new DefaultKey.Builder()
                                .type(submodel.getSemanticID().getKeys().get(0).getType())
                                .value(submodel.getSemanticID().getKeys().get(0).getValue())
                                .build())
                        .type(submodel.getSemanticID().getType())
                        .build())
                .idShort(submodel.getIdShort())
                .id(submodel.getId())
                .kind(submodel.getKind())
                .description(submodel.getDescription()).build();
        
        for(SubmodelElement element : submodel.getSubmodelElements()) {
                if(element instanceof SubmodelElementCollection){
                        
                }else if(element instanceof Blob){
                        
                }else if(element instanceof File){
                        
                }else if(element instanceof MultiLanguageProperty){
                        
                }else if(element instanceof Property){
                        copyProperty(builder, (Property) element);
                }else if(element instanceof Range){
                        
                }else if(element instanceof ReferenceElement){

                }
        }
        
        return builder.build();
    }

    private static Builder copyProperty(Builder builder, Property element) {
        builder.submodelElements(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(element.getSemanticID().getKeys().get(0).getType())
                                                .value(element.getSemanticID().getKeys().get(0).getValue())
                                                .build())
                                        .type(element.getSemanticID().getType())
                                        .build())
                                .supplementalSemanticIds(element.getSupplementalSemanticIds())
                                .displayName(element.getDisplayName())
                                .category(element.getCategory())
                                .description(element.getDescription())
                                .extensions(element.getExtensions())
                                .idShort(element.getIdShort())
                                .valueType(element.getValueType())
                                .value(element.getValue())
                                .valueID(element.getValueID())
                                .qualifiers(element.getQualifiers())
                                .embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                                .build());
        return builder;
    }

    
    public static Environment createCopy(Environment environment) {
        org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder builder = new DefaultEnvironment.Builder()
                .assetAdministrationShells(copyAAS(environment));
        environment.getSubmodels().forEach(submodel -> {
        builder.submodels(copySubmodel(submodel))
                .build();
        });
        builder.conceptDescriptions(environment.getConceptDescriptions());


        return builder.build();
    }

    /* AB HIER ALT */

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
                .kind(ModellingKind.INSTANCE)
                .description(Arrays.asList(
                        new DefaultLangStringTextType.Builder().text("The Submodel provides the means to access the Carbon Footprint of the asset.").language("en").build()
                ))
                //SMC ProductCarbonFootprint
                .submodelElements(new DefaultSubmodelElementCollection.Builder()
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
                        .build())
                //SMC TransportCarbonFootprint
                .submodelElements(new DefaultSubmodelElementCollection.Builder()
                .idShort(SMC_TRANSPORT_CARBON_FOOTPRINT_ID_SHORT)
                .description(Arrays.asList(
                new DefaultLangStringTextType.Builder().text("Balance of greenhouse gas emissions generated by a transport service of a product").language("en").build()
                ))
                .semanticID(new DefaultReference.Builder()
                        .keys(new DefaultKey.Builder()
                        .type(KeyTypes.GLOBAL_REFERENCE)
                        .value(SMC_TRANSPORT_CARBON_FOOTPRINT_SEMANTIC_ID)
                        .build())
                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                .build())
                .qualifiers(new DefaultQualifier.Builder()
                        .type("Cardinality")
                        .valueType(DataTypeDefXSD.STRING)
                        .value("ZeroToMany")
                        .build())
                        //TCFCalculationMethod
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG859#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Standard, method for determining the greenhouse gas emissions for the transport of a product").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("TCFCalculationMethod")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())
                        //TCFCO2eq
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG860#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Sum of all greenhouse gas emissions from vehicle operation").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("TCFCO2eq")
                                .valueType(DataTypeDefXSD.DOUBLE)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())
                        //TCFReferenceValueForCalculation
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG861#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Amount of product to which the TCF carbon footprint statement relates").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("TCFReferenceValueForCalculation")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())           
                        //TCFQuantityOfMeasureForCalculation
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG862#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Quantity of the product to which the TCF information on the CO2 footprint refers").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("TCFQuantityOfMeasureForCalculation")
                                .valueType(DataTypeDefXSD.DOUBLE)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())        
                        //TCFProcessesForGreenhouseGasEmissionInATransportService
                        .value(new DefaultProperty.Builder()
                                .semanticID(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                .value("0173-1#02-ABG863#001")
                                                .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build())
                                .description(Arrays.asList(
                                 new DefaultLangStringTextType.Builder().text("Processes in a transport service to determine the sum of all direct or indirect greenhouse gas emissions from fuel supply and vehicle operation").language("en").build()
                                ))
                                .category("PARAMETER")
                                .idShort("TCFProcessesForGreenhouseGasEmissionInATransportService")
                                .valueType(DataTypeDefXSD.STRING)
                                .value(" ")
                                .qualifiers(new DefaultQualifier.Builder()
                                .type("Cardinality")
                                .valueType(DataTypeDefXSD.STRING)
                                .value("One")
                                .build())
                                .build())  
                                
                        .build()) 
                .build();
    }

    public static ConceptDescription createConceptDescriptionExplanatoryStatement() {
        return new DefaultConceptDescription.Builder()
                .idShort("ExplanatoryStatement")
                .id("https://admin-shell.io/idta/CarbonFootprint/ExplanatoryStatement/1/0")
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("1")
                        .revision("0")
                        .build())
                .embeddedDataSpecifications(new DefaultEmbeddedDataSpecification.Builder()
                        .dataSpecification(new DefaultReference.Builder()
                                .keys(new DefaultKey.Builder()
                                        .type(KeyTypes.GLOBAL_REFERENCE)
                                        .value("http://admin-shell.io/DataSpecificationTemplates/DataSpecificationIEC61360/3/0")
                                        .build())
                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                .build())
                        .dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
                                .preferredName(new DefaultLangStringPreferredNameTypeIec61360.Builder().text("Erklärung").language("de").build())
                                .preferredName(new DefaultLangStringPreferredNameTypeIec61360.Builder().text("Explanatory statement").language("en").build())
                                .definition(new DefaultLangStringDefinitionTypeIec61360.Builder().text("Erforderliche oder vorhandene Erklärung, um sicherzustellen, dass eine Fußabdruckkommunikation von einem Käufer, potentiellen Käufer oder Anender des Produktes richtig verstanden werden kann").language("de").build())
                                .definition(new DefaultLangStringDefinitionTypeIec61360.Builder().text("Explanation which is needed or given so that a footprint communication can be properly understood by a purchaser, potential purchaser or user of the product").language("en").build())
                                .build())
                        .build())
                .build();
    }



    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
                .assetAdministrationShells(createAAS("testIdshort", "testId"))
                .submodels(createSubmodelCarbonFootprint())
                .conceptDescriptions(createConceptDescriptionExplanatoryStatement())
                .build();
    }

}
