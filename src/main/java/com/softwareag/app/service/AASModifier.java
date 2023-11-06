package com.softwareag.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultMultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultQualifier;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultRange;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;

public class AASModifier {

        private org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder assetAdministrationShellBuilder = null;
        private org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder environmentBuilder = null;
        private org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder submodelBuilder = null;

        private List<Submodel> submodels = new ArrayList<>();

       

        /* Asset Administration Shell */

        public AASModifier(
                        Environment environment) {

                this.environmentBuilder = new DefaultEnvironment.Builder();
                environmentBuilder.conceptDescriptions(environment.getConceptDescriptions()); //ConceptDescriptions not changing right now, can be copied by default

                this.assetAdministrationShellBuilder = new DefaultAssetAdministrationShell.Builder();
                AssetAdministrationShell shell = environment.getAssetAdministrationShells().get(0);
                this.assetAdministrationShellBuilder.idShort(shell.getIdShort())
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
                        this.assetAdministrationShellBuilder.submodels(new DefaultReference.Builder()
                                        .keys(new DefaultKey.Builder()
                                                        .type(KeyTypes.SUBMODEL)
                                                        .value(submodel.getId())
                                                        .build())
                                        .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                        .build());
                        this.submodels.add(cloneSubmodel(submodel).build());
                });
        }

        /* Submodels */

        private org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel.Builder cloneSubmodel(Submodel submodel) {

                this.submodelBuilder = new DefaultSubmodel.Builder();

                submodelBuilder.semanticID(submodel.getSemanticID())
                                .idShort(submodel.getIdShort())
                                .id(submodel.getId())
                                .kind(submodel.getKind())
                                .description(submodel.getDescription()).build();

                addElements(submodel.getSubmodelElements());

                return submodelBuilder;

        }

        private void addSubmodelToAASSubmodelList(String newSubmodelId) {

                this.assetAdministrationShellBuilder.submodels(new DefaultReference.Builder()
                                .keys(new DefaultKey.Builder()
                                                .type(KeyTypes.SUBMODEL)
                                                .value(newSubmodelId)
                                                .build())
                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                .build());
        }

        public AASModifier duplicateSubmodel(Submodel submodel, String newSubmodelId, String newSubmodelIdShort) {

                this.submodelBuilder = cloneSubmodel(submodel);

                this.submodelBuilder.id(newSubmodelId);
                this.submodelBuilder.idShort(newSubmodelIdShort);
                addSubmodelToAASSubmodelList(newSubmodelId);
                
                submodels.add(this.submodelBuilder.build());

                return this;
        }

        /* SubmodelElements */

        public AASModifier addCustomProperty(Submodel submodel, String idShort) {

                submodels.removeIf(model -> model.getIdShort().equals(submodel.getIdShort()));

                this.submodelBuilder = cloneSubmodel(submodel);

                submodelBuilder.submodelElements(new DefaultProperty.Builder()
                                                .semanticID(new DefaultReference.Builder()
                                                                .keys(new DefaultKey.Builder()
                                                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                                                .value("DUMMY")
                                                                                .build())
                                                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                                                .build())
                                                .qualifiers(new DefaultQualifier.Builder()
                                                        .value("0")
                                                        .type("Cardinality")
                                                        .valueType(DataTypeDefXSD.DOUBLE)
                                                        .build())
                                                .value("Dummy")
                                                .valueType(DataTypeDefXSD.STRING)
                                                .category("PARAMETER")
                                                .description(Arrays.asList(
                                                        new DefaultLangStringTextType.Builder().text("Das ist eine beispielhafte Beschreibung.").language("de").build()
                                                ))
                                                .idShort(idShort)
                                                .build());

                submodels.add(submodelBuilder.build());

                return this;
        }

        public AASModifier duplicateSubmodelElementCollection(Submodel submodel, String submodelElementCollectionIdShort, String newIdShort) {

                submodels.removeIf(model -> model.getIdShort().equals(submodel.getIdShort()));
                        
                this.submodelBuilder = cloneSubmodel(submodel);

                SubmodelElement elementCollection = submodel.getSubmodelElements().stream()
                        .filter(submodelElement -> submodelElement.getIdShort().equals(submodelElementCollectionIdShort))
                        .findFirst()
                        .map(this::cloneElement)
                        .orElse(null);
                
                elementCollection.setIdShort(newIdShort);
                /* elementCollection.setSemanticID(new DefaultReference.Builder()
                                                                .keys(new DefaultKey.Builder()
                                                                                .type(KeyTypes.GLOBAL_REFERENCE)
                                                                                .value("Test")
                                                                                .build())
                                                                .type(ReferenceTypes.EXTERNAL_REFERENCE)
                                                                .build()); */

                submodelBuilder.submodelElements(elementCollection);

                submodels.add(submodelBuilder.build());

                return this;
        }

        private void addElements(Collection<SubmodelElement> elements) {
                elements.forEach(element -> {
                        this.submodelBuilder.submodelElements(cloneElement(element));
                });
        }

        private SubmodelElement cloneElement(SubmodelElement element) {

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

        private Collection<SubmodelElement> cloneElements(Collection<SubmodelElement> elements) {
                Collection<SubmodelElement> copyOfElements = new ArrayList<>();
                elements.forEach(element -> {
                        copyOfElements.add(cloneElement(element));
                });
                return copyOfElements;
        }

        private SubmodelElementCollection cloneSubmodelElementCollection(SubmodelElementCollection element) {
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
                                .value(cloneElements(element.getValue()));
                return builder.build();
        }

        private Blob cloneBlob(Blob element) {
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

        private File cloneFile(File element) {
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

        private MultiLanguageProperty cloneMultiLanguageProperty(MultiLanguageProperty element) {
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

        private Property cloneProperty(Property element) {
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

        private Range cloneRange(Range element) {
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

        private ReferenceElement cloneReferenceElement(ReferenceElement element) {
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

        public Environment build() {
                this.environmentBuilder.assetAdministrationShells(this.assetAdministrationShellBuilder.build());
                submodels.forEach(submodel -> {
                        this.environmentBuilder.submodels(submodel);
                });
                return this.environmentBuilder.build();
        }

     /*   private org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder createAASCopy(
                        Environment environment,
                        org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell.Builder assetAdministrationShellBuilder) {
                org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment.Builder builder = new DefaultEnvironment.Builder()
                                .assetAdministrationShells(assetAdministrationShellBuilder.build());
                builder.conceptDescriptions(environment.getConceptDescriptions());
                return builder;
        }

        public Environment createCopyWithAddingCustomReferenceProperty(Environment environment, Submodel model) {
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

        public Environment createCopyWithAddingSubmodel(Environment environment, Submodel model,
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
        } */
}
