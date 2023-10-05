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
import java.util.Queue;

import javax.naming.InvalidNameException;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.aasx.InMemoryFile;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;

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
    public void updateProperty(String value, SubmodelType submodelType, SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {
        try {
            getSubmodels().stream()
                    .map(submodelElement -> (SubmodelElementCollection) getCertainSubmodelElementCollection(submodelType,
                            submodelElementCollections))
                    .flatMap(submodelElementCollection -> submodelElementCollection.getValue().stream())
                    .filter(element -> isProperty(element, propertyType))
                    .map(element -> (Property) element)
                    .forEach(property -> property.setValue(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Map ist für Transformation der Elemente, flatMap erstellt einen neuen Stream,
        // Filter ist wie eine Abfrage
    }

    public void updateFile(String path, SubmodelType submodelType, SubmodelElementPropertyType propertyType,
            SubmodelElementCollectionType... submodelElementCollections) {
        try {
            getSubmodels().stream()
                    .map(submodelElement -> (SubmodelElementCollection) getCertainSubmodelElementCollection(submodelType,
                            submodelElementCollections))
                    .flatMap(submodelElementCollection -> submodelElementCollection.getValue().stream())
                    .filter(element -> isFile(element, propertyType))
                    .map(element -> (File) element)
                    .forEach(property -> property.setValue(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SubmodelElementCollection getCertainSubmodelElementCollection(SubmodelType submodelType, SubmodelElementCollectionType... collections) {

        Queue<SubmodelElementCollectionType> collectionQueue = new LinkedList<>();
        for (SubmodelElementCollectionType collection : collections)
            collectionQueue.offer(collection);

        SubmodelElementCollection submodelElement = null;

        for (Submodel submodel : getSubmodels()) {
            if (!isSubmodelTypeOf(submodel, submodelType))
                continue;
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

        return null;

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
        throw new IllegalArgumentException("Wrong submodel, submodel must contain CarbonFootprint!");
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
