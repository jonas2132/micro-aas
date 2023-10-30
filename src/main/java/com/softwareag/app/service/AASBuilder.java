package com.softwareag.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Blob;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXSD;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.File;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Range;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder;

public class AASBuilder {

        /* Asset Administration Shell */

        private static org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder copyAAS(
                        Environment environment) {
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
                return builder;
        }

        private static org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder copyAASWithAddingSubmodel(
                        Environment environment, String newSubmodelId) {

                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder builder = copyAAS(
                                environment)
                                .submodels(new DefaultReference.Builder()
                                                .keys(new DefaultKey.Builder()
                                                                .type(KeyTypes.SUBMODEL)
                                                                .value(newSubmodelId)
                                                                .build())
                                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                                .build());

                return builder;
        }

        /* Submodels */

        private static org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder copySubmodel(
                        Submodel submodel) {
                Builder builder = new DefaultSubmodel.Builder();

                builder.semanticID(submodel.getSemanticID())
                                .idShort(submodel.getIdShort())
                                .id(submodel.getId())
                                .kind(submodel.getKind())
                                .description(submodel.getDescription()).build();

                copyElements(builder, submodel.getSubmodelElements());

                return builder;
        }

        private static org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder copySubmodel(
                        Submodel submodel, String newIdShort, String newId) {
                Builder builder = new DefaultSubmodel.Builder();
                builder.semanticID(submodel.getSemanticID())
                                .idShort(newIdShort)
                                .id(newId)
                                .kind(submodel.getKind())
                                .description(submodel.getDescription()).build();

                copyElements(builder, submodel.getSubmodelElements());

                return builder;
        }

        /* SubmodelElements */

        private static void copyElements(Builder builder, Collection<SubmodelElement> elements) {
                //SubmodelElements morgen manuell erstellen
                elements.forEach(element -> {

                        if (element instanceof SubmodelElementCollection) {
                                builder.submodelElements((SubmodelElementCollection) element);
                        } else if (element instanceof Blob) {
                                builder.submodelElements((Blob) element);
                        } else if (element instanceof File) {
                                builder.submodelElements((File) element);
                        } else if (element instanceof MultiLanguageProperty) {
                                builder.submodelElements((MultiLanguageProperty) element);
                        } else if (element instanceof Property) {
                                builder.submodelElements((Property) element);
                        } else if (element instanceof Range) {
                                builder.submodelElements((Range) element);
                        } else if (element instanceof ReferenceElement) {
                                builder.submodelElements((ReferenceElement) element);
                        }

                });
        }

        /* Environment */

        private static org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder createAASCopy(
                        Environment environment,
                        org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder assetAdministrationShellBuilder) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder builder = new DefaultEnvironment.Builder()
                                .assetAdministrationShells(assetAdministrationShellBuilder.build());
                builder.conceptDescriptions(environment.getConceptDescriptions());
                return builder;
        }

        public static Environment createCopyWithAddingCustomReferenceProperty(Environment environment, Submodel model) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder builder = createAASCopy(
                                environment, copyAAS(environment));

                environment.getSubmodels().forEach(submodel -> {

                        org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder submodelCopy = copySubmodel(
                                        submodel);

                        if (model.getIdShort().equals(submodel.getIdShort())) {

                                submodelCopy.submodelElements(new DefaultProperty.Builder()
                                                .semanticID(new DefaultReference.Builder()
                                                                .keys(new DefaultKey.Builder()
                                                                                .type(KeyTypes.CONCEPT_DESCRIPTION)
                                                                                .value("0000")
                                                                                .build())
                                                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                                                .build())
                                                .idShort("ReferenceProperty")
                                                .valueType(DataTypeDefXSD.STRING)
                                                .build());

                        }

                        builder.submodels(submodelCopy.build())
                                        .build();
                });

                return builder.build();
        }

        public static Environment createCopyWithAddingSubmodel(Environment environment, Submodel model,
                        String newIdShort, String newId) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder builder = createAASCopy(
                                environment, copyAASWithAddingSubmodel(environment, newId));
                environment.getSubmodels().forEach(submodel -> {
                        builder.submodels(copySubmodel(submodel).build())
                                        .build();
                });
                builder.submodels(copySubmodel(model, newIdShort, newId).build())
                                .build();

                return builder.build();
        }
}
