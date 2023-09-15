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

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;

public class EnvironmentService implements Environment{

    private Environment environment;
    private final String submodelName = "CarbonFootprint";
    private final String submodelElementCollectionName = "ProductCarbonFootprint";
    private final String propertyName = "PCFCO2eq";

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

    /**
     * Update the value of the "PCFCO2eq" property within the specified submodel and collection.
     *
     * @param newCO2eq The new value to set for the "PCFCO2eq" property.
     * 
     */
    public void updatePCFCO2eq(String newCO2eq) {
        /* for (Submodel submodel : getSubmodels()) {
            if (submodel.getIdShort().equals(this.submodelName)) {
                for (SubmodelElement submodelElement : submodel.getSubmodelElements()) {
                    if (submodelElement.getIdShort().equals(this.submodelElementCollectionName)) {
                        if (submodelElement instanceof SubmodelElementCollection) {
                            SubmodelElementCollection submodelElementCollection = (SubmodelElementCollection) submodelElement;
                            for (SubmodelElement element : submodelElementCollection.getValue()) {
                                if (element instanceof Property && element.getIdShort().equals(propertyName)) {
                                    Property property = (Property) element;
                                    property.setValue(newCO2eq);
                                }
                            }
                        }
                    }
                }
            }
        } */

        getSubmodels().stream()
        .filter(submodel -> submodel.getIdShort().equals(this.submodelName))
        .flatMap(submodel -> submodel.getSubmodelElements().stream())
        .filter(submodelElement -> submodelElement.getIdShort().equals(this.submodelElementCollectionName))
        .filter(submodelElement -> submodelElement instanceof SubmodelElementCollection)
        .map(submodelElement -> (SubmodelElementCollection) submodelElement)
        .flatMap(submodelElementCollection -> submodelElementCollection.getValue().stream())
        .filter(element -> element instanceof Property && element.getIdShort().equals(propertyName))
        .map(element -> (Property) element)
        .forEach(property -> property.setValue(newCO2eq));

    }
    
    
}
