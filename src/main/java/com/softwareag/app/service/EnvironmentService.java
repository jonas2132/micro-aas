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
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;

import com.softwareag.app.data.SubmodelElementPropertyType;

public class EnvironmentService implements Environment {

    private Environment environment;
    private List<InMemoryFile> fileList = new ArrayList<>();

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
    public EnvironmentService clone() {
        return new EnvironmentService(new AASModifier(environment).build());
    }

    public void duplicateSubmodel(String submodelIdShort, String newSubmodelId, String newSubmodelIdShort) {
        this.environment = new AASModifier(this.environment)
                .duplicateSubmodel(getSubmodelOfIdShort(submodelIdShort), newSubmodelId, newSubmodelIdShort).build();
        // this.environment = AASModifier.createCopyWithAddingSubmodel(environment,
        // getSubmodelOfIdShort(submodelIdShort), newSubmodelIdShort,
        // "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0/"+newSubmodelIdShort);
    }

    public void duplicateSubmodelElementCollection(String submodelIdShort, String submodelElementCollectionIdShort,
            String newIdShort) {
        this.environment = new AASModifier(environment)
                .duplicateSubmodelElementCollection(getSubmodelOfIdShort(submodelIdShort),
                        submodelElementCollectionIdShort, newIdShort)
                .build();
    }

    public void addCustomProperty(String submodelIdShort, String popertyIdShort) {
        this.environment = new AASModifier(environment)
                .addCustomProperty(getSubmodelOfIdShort(submodelIdShort), popertyIdShort)
                .build();
    }

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

    public void updateMultilanguageProperty(String value, String submodelIdShort,
            SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {
        System.out.println("Updating MultilanguageProperty " + propertyType.getIdShort() + " . . .");
        try {
            List<LangStringTextType> valueList = new ArrayList<>();
            DefaultLangStringTextType stringTextType = new DefaultLangStringTextType();

            stringTextType.setLanguage("de");
            stringTextType.setText(value);
            valueList.add(0, stringTextType);

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

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

    public List<LangStringTextType> getMultilanguageProperty(String submodelIdShort,
            SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {
        System.out.println("Reading MultilanguageProperty " + propertyType.getIdShort() + " . . .");
        try {
            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

            return subModelElements.stream()
                    .filter(element -> isMultilanguageProperty(element, propertyType))
                    .map(element -> ((MultiLanguageProperty) element).getValue())
                    .findFirst().orElse(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        // Map ist für Transformation der Elemente, flatMap erstellt einen neuen Stream,
        // Filter ist wie eine Abfrage
    }

    public void updateProperty(String value, String submodelIdShort, SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {
        System.out.println("Updating Property " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isProperty(element, propertyType))
                    .map(element -> (Property) element)
                    .forEach(property -> property.setValue(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateReferenceElement(String value, String submodelIdShort, SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {
        System.out.println("Updating ReferenceElement " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isReferenceElement(element, propertyType))
                    .map(element -> (ReferenceElement) element)
                    .forEach(referenceElement -> referenceElement.setValue(new DefaultReference.Builder()
                            .keys(new DefaultKey.Builder()
                                            .type(KeyTypes.SUBMODEL) //Not sure yet, if Submodel is used as reference!
                                            .value(value)
                                            .build())
                            .type(ReferenceTypes.EXTERNAL_REFERENCE)
                            .build()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateFile(String path, String submodelIdShort, SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {
        System.out.println("Updating File " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

            subModelElements.stream()
                    .filter(element -> isFile(element, propertyType))
                    .map(element -> (File) element)
                    .forEach(property -> property.setValue(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPropertyValue(String submodelIdShort, SubmodelElementPropertyType propertyType,
            String... submodelElementCollections) {

        System.out.println("Reading Property " + propertyType.getIdShort() + " . . .");
        try {

            Collection<SubmodelElement> subModelElements = getSubmodelElements(submodelIdShort,
                    submodelElementCollections);

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

    // no final solution yet, but rn its not possible to call the enums on the
    // frontend
    public String getPCFCO2() {
        return getPropertyValue("CarbonFootprint", SubmodelElementPropertyType.PCFCO2EQ,
                "ProductCarbonFootprint");
    }

    public String getTCFCO2() {
        return getPropertyValue("CarbonFootprint", SubmodelElementPropertyType.TCFCO2EQ,
                "TransportCarbonFootprint");
    }

    private Collection<SubmodelElement> getSubmodelElements(String submodelIdShort,
            String... submodelElementCollections) {

        SubmodelElementCollection submodelElementCollection = (SubmodelElementCollection) getCertainSubmodelElementCollection(
                submodelIdShort, submodelElementCollections);
        Collection<SubmodelElement> subModelElements = submodelElementCollection != null
                ? submodelElementCollection.getValue()
                : getSubmodelOfIdShort(submodelIdShort).getSubmodelElements();
        return subModelElements;

    }

    public SubmodelElementCollection getCertainSubmodelElementCollection(String submodelIdShort,
            String... collections) {

        Queue<String> collectionQueue = new LinkedList<>();
        for (String collection : collections)
            collectionQueue.offer(collection);

        SubmodelElementCollection submodelElement = null;
        Submodel submodel = getSubmodelOfIdShort(submodelIdShort);

        while (!collectionQueue.isEmpty()) {
            Iterable<SubmodelElement> elements = collections.length == collectionQueue.size()
                    ? submodel.getSubmodelElements()
                    : submodelElement.getValue(); // bei erstem Element anderer Zugriff
            for (SubmodelElement element : elements) {
                if (!(element instanceof SubmodelElementCollection))
                    continue;
                if (isNextSubmodelElementCollection(collectionQueue, element)) {
                    submodelElement = (SubmodelElementCollection) element;
                    break;
                }
            }
        }
        return submodelElement;

    }


    public Submodel getSubmodelOfIdShort(String submodelIdShort) {
        return getSubmodels().stream()
                .filter(submodel -> isSubmodelTypeOf(submodel, submodelIdShort))
                .findFirst()
                .orElse(null);
    }

    public boolean isMultilanguageProperty(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof MultiLanguageProperty
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }
    

    public boolean isProperty(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof Property
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    public boolean isReferenceElement(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof ReferenceElement
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    public boolean isFile(SubmodelElement submodelElement,
            SubmodelElementPropertyType submodelElementPropertyType) {
        return submodelElement instanceof File
                && submodelElement.getIdShort().equals(submodelElementPropertyType.getIdShort());
    }

    private boolean isSubmodelTypeOf(Submodel submodel, String submodelIdShort) throws IllegalArgumentException {
        if (submodel.getIdShort().equals(submodelIdShort))
            return true;
        return false;
    }

    private boolean isNextSubmodelElementCollection(Queue<String> collectionQueue,
            SubmodelElement submodelElement) {
        if (submodelElement.getIdShort().equals(collectionQueue.peek())
                && submodelElement instanceof SubmodelElementCollection) {
            collectionQueue.poll();
            return true;
        }
        return false;
    }

}
