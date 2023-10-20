/**
 * EnvironmentService - A class for managing and updating digital twin environment data.
 * This class extends DefaultEnvironment, which is part of the Eclipse Digital Twin AAS4J library.
 *
 * The EnvironmentService class is designed to work with an existing digital twin environment,
 * asset administration shell, and submodels to update specific properties within the environment.
 *
 * Features:
 * - Update the value of a specific property within a submodel.
 *
 * Usage:
 * 1. Create an instance of EnvironmentService by providing an Environment object.
 * 2. Use the `updatePCFCO2eq` method to update the value of the "PCFCO2eq" property within the
 *    "CarbonFootprint" submodel's "ProductCarbonFootprint" collection.
 *
 * Example:
 * ```
 * EnvironmentService environmentService = new EnvironmentService(existingEnvironment);
 * environmentService.updatePCFCO2eq("NewCO2Value");
 * ```
 *
 * Dependencies:
 * - Eclipse Digital Twin AAS4J library (org.eclipse.digitaltwin.aas4j.v3.model)
 *
 * Note:
 * - Ensure that the provided Environment object contains the necessary digital twin structure.
 * - The class assumes that there is only one Asset Administration Shell within the environment, this could be
 *   a problem in the future. Let's keep an eye on it.
 *
 * @author [DevAZK]
 * @version 1.0
 */
package com.softwareag.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
//Decorator Pattern
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.naming.InvalidNameException;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;

import com.softwareag.app.data.SubmodelElementCollectionType;
import com.softwareag.app.data.SubmodelElementPropertyType;
import com.softwareag.app.data.SubmodelType;

public class EnvironmentService implements Environment {

    private Environment environment;
    private List<InMemoryFile> fileList = new ArrayList<>();
    private final String submodelName = "CarbonFootprint";
    private final String submodelElementCollectionNamePCF = "ProductCarbonFootprint";
    private final String propertyNamePCF = "PCFCO2eq";

    public EnvironmentService(Environment environment) {
        this.environment = environment;
    }

    @Override
    public List<AssetAdministrationShell> getAssetAdministrationShells() {
        return environment.getAssetAdministrationShells();
    }

    @Override
    public void setAssetAdministrationShells(List<AssetAdministrationShell> assetAdministrationShells) {
        environment.setAssetAdministrationShells(assetAdministrationShells);
    }

    @Override
    public List<ConceptDescription> getConceptDescriptions() {
        return environment.getConceptDescriptions();
    }

    @Override
    public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
        environment.setConceptDescriptions(conceptDescriptions);
    }

    @Override
    public List<Submodel> getSubmodels() {
        return environment.getSubmodels();
    }

    @Override
    public void setSubmodels(List<Submodel> submodels) {
        environment.setSubmodels(submodels);
    }

    public Environment getEnvironmentInstance() {
        return this.environment;
    }

    public void setFilelist(List<InMemoryFile> fileList) {
        this.fileList = fileList;
    }

    public List<InMemoryFile> getFileList() {
        return this.fileList;
    }

    /**
     * TO DO - Documentation
     *
     * 
     * 
     */
    public void updateAssetID(String value) {
        getAssetAdministrationShells().get(0).setId(value);
    }

    public void updateAssetIDShort(String value) {
        getAssetAdministrationShells().get(0).setIdShort(value);
    }

    public String getAssetID() {
        return getAssetAdministrationShells().get(0).getId();
    }

    public String getAssetIDShort() {
        return getAssetAdministrationShells().get(0).getIdShort();
    }

    /*
    private Submodel getSubmodelCopy(SubmodelType submodelType, String newId, String newIdShort) {
        Submodel submodel = getSubmodelOfType(submodelType);
        DefaultSubmodel submodelCopy = new DefaultSubmodel();

        submodelCopy.setQualifiers(submodel.getQualifiers());
        submodelCopy.setKind(submodel.getKind());
        submodelCopy.setSemanticID(submodel.getSemanticID());
        submodelCopy.setSupplementalSemanticIds(submodel.getSupplementalSemanticIds());
        submodelCopy.setAdministration(submodel.getAdministration());
        submodelCopy.setId(newId);
        submodelCopy.setCategory(submodel.getCategory());
        submodelCopy.setDescription(submodel.getDescription());
        submodelCopy.setDisplayName(submodel.getDisplayName());
        submodelCopy.setIdShort(newIdShort);
        submodelCopy.setExtensions(submodel.getExtensions());
        submodelCopy.setEmbeddedDataSpecifications(submodel.getEmbeddedDataSpecifications());
        submodelCopy.setSubmodelElements(submodel.getSubmodelElements());

        return submodelCopy;
    } */


    public void updateMultilanguageProperty(String value, SubmodelType submodelType,
            SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {
        System.out.println("Updating MultilanguageProperty " + propertyType.getIdShort() + " . . .");
        try {
            List<LangStringTextType> valueList = new ArrayList<>();
            DefaultLangStringTextType stringTextType = new DefaultLangStringTextType();

            stringTextType.setLanguage("de");
            stringTextType.setText(value);
            valueList.add(0, stringTextType);

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelType, submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isMultilanguageProperty(element, propertyType))
                    .map(element -> (MultiLanguageProperty) element)
                    .forEach(property -> property.setValue(valueList));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Map ist für Transformation der Elemente, flatMap erstellt einen neuen Stream,
        // Filter ist wie eine Abfrage
    }

    public void updateProperty(String value, SubmodelType submodelType, SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {
        System.out.println("Updating Property " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelType, submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isProperty(element, propertyType))
                    .map(element -> (Property) element)
                    .forEach(property -> property.setValue(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateFile(String path, SubmodelType submodelType, SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {
        System.out.println("Updating File " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelType, submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isFile(element, propertyType))
                    .map(element -> (File) element)
                    .forEach(property -> property.setValue(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPropertyValue(SubmodelType submodelType, SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {

        System.out.println("Reading Property " + propertyType.getIdShort() + " . . .");
        try {
            
            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelType, submodelElementCollections);

            return subModelElements.stream()
                    .filter(element -> isProperty(element, propertyType))
                    .map(element -> ((Property) element).getValue())
                    .findFirst()
                    .map(Object::toString)
                    .orElse("0");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
    }


    // no final solution yet, but rn its not possible to call the enums on the frontend
    public String getPCFCO2(){
        return getPropertyValue(SubmodelType.CARBON_FOOTPRINT, SubmodelElementPropertyType.PCFCO2EQ , SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
    }

    public String getTCFCO2(){
        return getPropertyValue(SubmodelType.CARBON_FOOTPRINT, SubmodelElementPropertyType.TCFCO2EQ , SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
    }
    //

    private Collection<SubmodelElement> getSubmodelElements(SubmodelType submodelType, SubmodelElementCollectionType... submodelElementCollections) {

        SubmodelElementCollection submodelElementCollection = (SubmodelElementCollection) getCertainSubmodelElementCollection(
                    submodelType, submodelElementCollections);
        Collection<SubmodelElement> subModelElements = submodelElementCollection != null
                ? submodelElementCollection.getValue()
                : getSubmodelOfType(submodelType).getSubmodelElements();
        return subModelElements;

    }

    private SubmodelElementCollection getCertainSubmodelElementCollection(SubmodelType submodelType,
            SubmodelElementCollectionType... collections) {

        Queue<SubmodelElementCollectionType> collectionQueue = new LinkedList<>();
        for (SubmodelElementCollectionType collection : collections)
            collectionQueue.offer(collection);

        SubmodelElementCollection submodelElement = null;
        Submodel submodel = getSubmodelOfType(submodelType);

        while (!collectionQueue.isEmpty()) {
            Iterable<SubmodelElement> elements = collections.length == collectionQueue.size()
                    ? submodel.getSubmodelElements()
                    : submodelElement.getValue(); // bei erstem Element anderer Zugriff
            for (SubmodelElement element : elements) {
                if (!(element instanceof SubmodelElementCollection))
                    continue;
                if (isSubmodelElementCollection(collectionQueue, element)) {
                    submodelElement = (SubmodelElementCollection) element;
                    break;
                }
            }
        }
        return submodelElement;

    }

    private Submodel getSubmodelOfType(SubmodelType submodelType) {
        for (Submodel submodel : getSubmodels()) {
            if (!isSubmodelTypeOf(submodel, submodelType))
                continue;
            return submodel;
        }
        return null;
    }

    private boolean isMultilanguageProperty(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof MultiLanguageProperty
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    private boolean isProperty(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof Property
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    private boolean isFile(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof File
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    private boolean isSubmodelTypeOf(Submodel submodel, SubmodelType submodelType) throws IllegalArgumentException {
        if (submodel.getIdShort().equals(submodelType.getIdShort()))
            return true;
        return false;
    }

    private boolean isSubmodelElementCollection(Queue<SubmodelElementCollectionType> collectionQueue,
            SubmodelElement submodelElement) {

        SubmodelElementCollectionType collectionType = collectionQueue.peek();
        if (submodelElement.getIdShort().equals(collectionType.getIdShort())
                && submodelElement instanceof SubmodelElementCollection) {
            collectionQueue.poll();
            return true;
        }
        return false;
    }

}
