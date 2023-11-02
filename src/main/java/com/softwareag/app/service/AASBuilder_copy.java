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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultBlob;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultFile;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultMultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultRange;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder;

public class AASBuilder_copy {

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

                addElements(builder, submodel.getSubmodelElements());

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

                addElements(builder, submodel.getSubmodelElements());

                return builder;
        }

        /* SubmodelElements */

        private static void addElements(Builder builder, Collection<SubmodelElement> elements) {
                elements.forEach(element -> {
                        builder.submodelElements(copyElement(element));
                });
        }

        private static SubmodelElement copyElement(SubmodelElement element) {

                if (element instanceof SubmodelElementCollection) {
                        return cloneSubmodelElementCollection((SubmodelElementCollection) element);
                } else if (element instanceof Blob) {
                        return cloneBlob((Blob) element);
                } else if (element instanceof File) {
                        return cloneFile((File) element);
                } else if (element instanceof MultiLanguageProperty) {
                        return cloneMultiLanguageProperty((MultiLanguageProperty) element);
                } else if (element instanceof Property) {
                        return cloneProperty((Property) element);
                } else if (element instanceof Range) {
                        return cloneRange((Range) element);
                } else if (element instanceof ReferenceElement) {
                        return cloneReferenceElement((ReferenceElement) element);
                }

                return null;
        }

        private static Collection<SubmodelElement> copyElements(Collection<SubmodelElement> elements) {
                Collection<SubmodelElement> copyOfElements = new ArrayList<>();
                elements.forEach(element -> {
                        copyOfElements.add(copyElement(element));
                });
                return copyOfElements;
        }

        private static SubmodelElementCollection cloneSubmodelElementCollection(SubmodelElementCollection element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection.Builder builder = new DefaultSubmodelElementCollection.Builder();
                builder.embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                                .extensions(element.getExtensions())
                                .semanticID(element.getSemanticID())
                                .supplementalSemanticIds(element.getSupplementalSemanticIds())
                                .qualifiers(element.getQualifiers())
                                .category(element.getCategory())
                                .description(element.getDescription())
                                .displayName(element.getDisplayName())
                                .idShort(element.getIdShort())
                                .value(copyElements(element.getValue()));
                return builder.build();
        }

        private static Blob cloneBlob(Blob element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultBlob.Builder builder = new DefaultBlob.Builder();
                builder.contentType(element.getContentType())
                        .value(element.getValue())
                        .embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .qualifiers(element.getQualifiers())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort());
                return builder.build();
        }

        private static File cloneFile(File element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultFile.Builder builder = new DefaultFile.Builder();
                builder.contentType(element.getContentType())
                        .value(element.getValue())
                        .embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .qualifiers(element.getQualifiers())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort());
                return builder.build();
        }

        private static MultiLanguageProperty cloneMultiLanguageProperty(MultiLanguageProperty element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultMultiLanguageProperty.Builder builder = new DefaultMultiLanguageProperty.Builder();
                builder.embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .value(element.getValue())
                        .valueID(element.getValueID())
                        .qualifiers(element.getQualifiers())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort());
                return builder.build();
        }

        private static Property cloneProperty(Property element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty.Builder builder = new DefaultProperty.Builder();
                builder.embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .value(element.getValue())
                        .valueID(element.getValueID())
                        .valueType(element.getValueType())
                        .qualifiers(element.getQualifiers())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort());
                return builder.build();
        }

        private static Range cloneRange(Range element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultRange.Builder builder = new DefaultRange.Builder();
                builder.embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .qualifiers(element.getQualifiers())
                        .max(element.getMax())
                        .min(element.getMin())
                        .valueType(element.getValueType())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort());
                return builder.build();
        }
        
        private static ReferenceElement cloneReferenceElement(ReferenceElement element) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReferenceElement.Builder builder = new DefaultReferenceElement.Builder();
                builder.embeddedDataSpecifications(element.getEmbeddedDataSpecifications())
                        .extensions(element.getExtensions())
                        .semanticID(element.getSemanticID())
                        .supplementalSemanticIds(element.getSupplementalSemanticIds())
                        .qualifiers(element.getQualifiers())
                        .category(element.getCategory())
                        .description(element.getDescription())
                        .displayName(element.getDisplayName())
                        .idShort(element.getIdShort())
                        .value(element.getValue());
                return builder.build();
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
