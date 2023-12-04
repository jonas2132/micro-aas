package com.softwareag.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.softwareag.app.App;
import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.DataType;
import com.softwareag.app.data.JsonDataRepository;
import com.softwareag.app.data.SubmodelElementPropertyType;
import com.softwareag.app.service.DownloadService;
import com.softwareag.app.service.EnvironmentService;
import com.softwareag.app.utils.Constants;
import com.softwareag.app.service.EnvironmentService.*;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.eclipse.digitaltwin.aas4j.v3.model.*;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class WebController {

        private DataRepository jsonReaderRepository = new JsonDataRepository();
        private List<EnvironmentService> environmentServices = new ArrayList<>();
        private boolean existingFilesLoaded = false;

        @RequestMapping("/")
        public String redirectToWelcome() {
                return "redirect:/welcome";
        }

        @GetMapping("/welcome")
        public String welcomeView(Model model) {
                model.addAttribute("pageTitle", "AAS Builder");
                if (!existingFilesLoaded)
                        loadEnvironments();
                return "welcome"; // This corresponds to a view named "view.html" in your templates folder
        }

        @GetMapping("/aas/overview")
        public String showOverview(Model model) {
                model.addAttribute("pageTitle", "AAS Overview");
                model.addAttribute("environmentServices", environmentServices);
                if (!existingFilesLoaded)
                        loadEnvironments();
                return "overview";
        }

        @PostMapping("/aas/overview/import")
        public ResponseEntity<String> importAAS(Model model, @RequestParam("file") MultipartFile file) {
                if (!file.isEmpty()) {
                        try {
                                File importedFile = convertMultipartFileToFile(file);
                                String fileExtension = file.getOriginalFilename()
                                                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

                                if (!fileExtension.equals("aasx") && !fileExtension.equals("json")) {
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body("Invalid File format");
                                }

                                DataRepository dataRepository = fileExtension.equals("json") ? new JsonDataRepository()
                                                : new AASXDataRepository();

                                EnvironmentService environmentService = dataRepository
                                                .read(importedFile);
                                serializeEnvironment(environmentService);
                                environmentServices.add(environmentService);
                                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
                        } catch (FileSizeLimitExceededException e) {
                                // Handle exceptions if any
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body("File exceeds its maximum permitted size of 1048576 bytes");
                        } catch (Exception ex) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body("File upload failed.");
                        }
                } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file provided");
                }
        }

        @GetMapping("/aas/form")
        public String showForm(Model model) {
                model.addAttribute("pageTitle", "AAS Configurator");
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> environmentServicesIDs = new ArrayList<>();
                model.addAttribute("environmentServices", environmentServices);
                model.addAttribute("editMode", false);
                for (EnvironmentService serv : environmentServices) {
                        environmentServicesIDs.add(serv.getAssetID());
                }
                System.out.println(("environmentServiceIDs: " + environmentServicesIDs));
                String environmentServicesIDsJSON;
                try {
                        environmentServicesIDsJSON = objectMapper.writeValueAsString(environmentServicesIDs);
                        model.addAttribute("environmentServicesIDs", environmentServicesIDsJSON);

                } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return "aas_form";
        }

        @GetMapping("/aas/edit/{ID}")
        public String showEditForm(@PathVariable String ID, Model model) {

                ObjectMapper objectMapper = new ObjectMapper();
                List<String> prefillValues = new ArrayList<>();
                List<String> prefillValuesPCF = new ArrayList<>();
                List<String> prefillValuesTCF = new ArrayList<>();

                EnvironmentService envServiceToEdit = environmentServices.stream()
                                .filter(service -> service.getAssetIDShort().equals(ID))
                                .findFirst().get();

                // Getting a static amount of Nameplate and TechnicalData Values and add them to
                // the forwarded arrays.
                {
                        prefillValues.add(envServiceToEdit.getAssetIDShort());
                        prefillValues.add(envServiceToEdit.getAssetID());
                        prefillValues.add(envServiceToEdit.getPropertyValue("Nameplate",
                                        SubmodelElementPropertyType.URI_OF_THE_PRODUCT));
                        prefillValues.add(envServiceToEdit
                                        .getPropertyValue("Nameplate",
                                                        SubmodelElementPropertyType.MANUFACTURER_NAME)
                                        );
                        prefillValues.add(envServiceToEdit.getPropertyValue("Nameplate",
                                        SubmodelElementPropertyType.SERIAL_NUMBER));
                        prefillValues.add(envServiceToEdit.getPropertyValue("Nameplate",
                                        SubmodelElementPropertyType.YEAR_OF_CONSTRUCTION));
                        prefillValues.add(envServiceToEdit.getPropertyValue("Nameplate",
                                        SubmodelElementPropertyType.DATE_OF_MANUFACTURE));
                        prefillValues.add(envServiceToEdit.getPropertyValue("TechnicalData",
                                        SubmodelElementPropertyType.MANUFACTURER_ORDER_CODE, "GeneralInformation"));
                }

                Submodel certainSubmodel = envServiceToEdit.getSubmodelOfIdShort("CarbonFootprint");

                // Getting a dynamic amount of PCF Values and add them to forwarded arrays.
                for (SubmodelElementCollection smcPCF : certainSubmodel.getSubmodelElements().stream()
                                .filter(element -> element instanceof SubmodelElementCollection)
                                .map(element -> (SubmodelElementCollection) element)
                                .filter(element -> element.getIdShort().startsWith("ProductCarbonFootprint"))
                                .collect(Collectors.toList())) {
                        String smcPCFID = smcPCF.getIdShort();
                        prefillValuesPCF.add(envServiceToEdit.getReferenceElement("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_REFERENCE, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_CALCULATION_METHOD, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCFCO2EQ, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_REFERENCE_VALUE_FOR_CALCULATION, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE, smcPCFID));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_DESCRIPTION, smcPCFID));                                        
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, smcPCFID, "PCFGoodsAddressHandover"));
                        prefillValuesPCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, smcPCFID, "PCFGoodsAddressHandover"));
                }

                // Getting a dynamic amount of TCF Values and add them to forwarded arrays.
                for (SubmodelElementCollection smcTCF : certainSubmodel.getSubmodelElements().stream()
                                .filter(element -> element instanceof SubmodelElementCollection)
                                .map(element -> (SubmodelElementCollection) element)
                                .filter(element -> element.getIdShort().startsWith("TransportCarbonFootprint"))
                                .collect(Collectors.toList())) {
                        String smcTCFID = smcTCF.getIdShort();
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_CALCULATION_METHOD, smcTCFID));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.TCFCO2EQ, smcTCFID));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION, smcTCFID));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION, smcTCFID));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_PROCESSES_FOR_GREENHOUSE_GAS_EMISSION_IN_A_TRANSPORT_SERVICE,
                                        smcTCFID));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, smcTCFID,
                                        "TCFGoodsTransportAddressTakeover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));
                        prefillValuesTCF.add(envServiceToEdit.getPropertyValue("CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, smcTCFID,
                                        "TCFGoodsTransportAddressHandover"));

                }

                // Converting all arrays to forward into the JSON format and then adding them to
                // the Spring Model
                try {
                        model.addAttribute("pageTitle", "AAS Configurator");
                        model.addAttribute("environmentServices", environmentServices);
                        model.addAttribute("editMode", true);
                        String prefillValuesJSON = objectMapper.writeValueAsString(prefillValues);
                        String prefillValuesPCFJSON = objectMapper.writeValueAsString(prefillValuesPCF);
                        String prefillValuesTCFJSON = objectMapper.writeValueAsString(prefillValuesTCF);
                        model.addAttribute("prefillValues", prefillValuesJSON);
                        model.addAttribute("prefillValuesPCF", prefillValuesPCFJSON);
                        model.addAttribute("prefillValuesTCF", prefillValuesTCFJSON);

                } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return "aas_form";
        }

        @GetMapping("/aas/delete/{ID}")
        public String deleteAAS(@PathVariable String ID, Model model) {
                model.addAttribute("pageTitle", "AAS Configurator");
                EnvironmentService toDelete = environmentServices.stream()
                                .filter(envServ -> envServ.getAssetIDShort().equals(ID))
                                .findFirst()
                                .orElse(null);
                deleteEnvironment(toDelete);
                environmentServices.remove(toDelete);
                return "redirect:/aas/overview";
        }

        @PostMapping("/aas/submission")
        public String submission(
                        // parameter Asset Administration Shell
                        @RequestParam(value = "assetIDshort", defaultValue = " ") String assetIDshort, // includes actual AssetIDShort of AAS and
                                                                           // CarbonFootprint Submodel assetIDshorts
                        @RequestParam("assetID") String assetID,

                        // parameter Nameplate
                        @RequestParam(value = "URIOfTheProduct", defaultValue = " ") String URIOfTheProduct,
                        @RequestParam(value = "ManufacturerName", defaultValue = " ") String ManufacturerName,
                        @RequestParam(value = "SerialNumber", defaultValue = " ") String SerialNumber,
                        @RequestParam(value = "YearOfConstruction", defaultValue = " ") String YearOfConstruction,
                        @RequestParam(value = "DateOfManufacture", defaultValue = " ") String DateOfManufacture,
                        // parameter Technical Data
                        @RequestParam(value = "ManufacturerOrderCode", defaultValue = " ") String ManufacturerOrderCode,
                        // @RequestParam("ManufacturerLogo") File ManufacturerLogo,
                        // @RequestParam("ProductImage") File ProductImage,

                        // Parameter Carbon Footprint
                        // Parameter Product Carbon Footprint
                        @RequestParam(value = "ReferableAssetID", defaultValue = " ") String[] ReferableAssetID,
                        @RequestParam(value = "PCFCalculationMethod", defaultValue = " ") String[] PCFCalculationMethod,
                        @RequestParam(value = "PCFCO2eq", defaultValue = " ") String[] PCFCO2eq,
                        @RequestParam(value = "PCFQuantityOfMeasureForCalculation", defaultValue = " ") String[] PCFQuantityOfMeasureForCalculation,
                        @RequestParam(value = "PCFReferenceValueForCalculation", defaultValue = " ") String[] PCFReferenceValueForCalculation,
                        @RequestParam(value = "PCFLiveCyclePhase", defaultValue = " ") String[] PCFLiveCyclePhase,
                        @RequestParam(value = "PCFDescription", defaultValue = " ") String[] PCFDescription,
                        // @RequestParam("ExplanatoryStatement") File[] ExplanatoryStatement,
                        @RequestParam(value = "PCFHandoverStreet", defaultValue = " ") String[] PCFHandoverStreet,
                        @RequestParam(value = "PCFHandoverNumber", defaultValue = " ") String[] PCFHandoverNumber,
                        @RequestParam(value = "PCFHandoverCity", defaultValue = " ") String[] PCFHandoverCity,
                        @RequestParam(value = "PCFHandoverZIP", defaultValue = " ") String[] PCFHandoverZIP,
                        @RequestParam(value = "PCFHandoverCountry", defaultValue = " ") String[] PCFHandoverCountry,
                        @RequestParam(value = "PCFHandoverLatitude", defaultValue = " ") String[] PCFHandoverLatitude,
                        @RequestParam(value = "PCFHandoverStreet", defaultValue = " ") String[] PCFHandoverLongitude,

                        // Parameter Transport Carbon Footprint
                        @RequestParam(value = "TCFCalculationMethod", defaultValue = " ") String[] TCFCalculationMethod,
                        @RequestParam(value = "TCFCO2eq", defaultValue = " ") String[] TCFCO2eq,
                        @RequestParam(value = "TCFReferenceValueForCalculation", defaultValue = " ") String[] TCFReferenceValueForCalculation,
                        @RequestParam(value = "TCFQuantityOfMeasureForCalculation", defaultValue = " ") String[] TCFQuantityOfMeasureForCalculation,
                        @RequestParam(value = "TCFProcessesForGreenhouseGasEmissionInATransportService", defaultValue = " ") String[] TCFProcessesForGreenhouseGasEmissionInATransportService,
                        @RequestParam(value = "TCFTakeoverStreet", defaultValue = " ") String[] TCFTakeoverStreet,
                        @RequestParam(value = "TCFTakeoverNumber", defaultValue = " ") String[] TCFTakeoverNumber,
                        @RequestParam(value = "TCFTakeoverCity", defaultValue = " ") String[] TCFTakeoverCity,
                        @RequestParam(value = "TCFTakeoverZIP", defaultValue = " ") String[] TCFTakeoverZIP,
                        @RequestParam(value = "TCFTakeoverCountry", defaultValue = " ") String[] TCFTakeoverCountry,
                        @RequestParam(value = "TCFTakeoverLatitude", defaultValue = " ") String[] TCFTakeoverLatitude,
                        @RequestParam(value = "TCFTakeoverLongitude", defaultValue = " ") String[] TCFTakeoverLongitude,
                        @RequestParam(value = "TCFHandoverStreet", defaultValue = " ") String[] TCFHandoverStreet,
                        @RequestParam(value = "TCFHandoverNumber", defaultValue = " ") String[] TCFHandoverNumber,
                        @RequestParam(value = "TCFHandoverCity", defaultValue = " ") String[] TCFHandoverCity,
                        @RequestParam(value = "TCFHandoverZIP", defaultValue = " ") String[] TCFHandoverZIP,
                        @RequestParam(value = "TCFHandoverCountry", defaultValue = " ") String[] TCFHandoverCountry,
                        @RequestParam(value = "TCFHandoverLatitude", defaultValue = " ") String[] TCFHandoverLatitude,
                        @RequestParam(value = "TCFHandoverLongitude", defaultValue = " ") String[] TCFHandoverLongitude) {

                // Deletes old environmentServices with the corresponding assedID to overwrite
                // it with the new values
                environmentServices.removeIf(service -> service.getAssetID().equals(assetID));

                EnvironmentService environmentService = jsonReaderRepository
                                .read(new File(Constants.RESOURCE_DIRECTORY + "/" + "FullAASTemplate_custom.json"));

                environmentService.updateAssetIDShort(assetIDshort);
                environmentService.updateAssetID(assetID);

                /* NAMEPLATE */
                environmentService.updateProperty(URIOfTheProduct, "Nameplate",
                                SubmodelElementPropertyType.URI_OF_THE_PRODUCT);
                environmentService.updateMultilanguageProperty(ManufacturerName, "Nameplate",
                                SubmodelElementPropertyType.MANUFACTURER_NAME);
                environmentService.updateProperty(SerialNumber, "Nameplate",
                                SubmodelElementPropertyType.SERIAL_NUMBER);
                environmentService.updateProperty(YearOfConstruction, "Nameplate",
                                SubmodelElementPropertyType.YEAR_OF_CONSTRUCTION);
                environmentService.updateProperty(DateOfManufacture, "Nameplate",
                                SubmodelElementPropertyType.DATE_OF_MANUFACTURE);

                /* TECHNICAL DATA */
                environmentService.updateProperty(ManufacturerOrderCode, "TechnicalData",
                                SubmodelElementPropertyType.MANUFACTURER_ORDER_CODE, "GeneralInformation");

                /* PRODUCT CARBON FOOTPRINT */
                for (int i = 0; i < PCFCalculationMethod.length; i++) {
                        String submodelElementCollectionIdShort = "ProductCarbonFootprint";

                        if (i > 0) {
                                submodelElementCollectionIdShort += "_" + (i + 1);
                                environmentService.duplicateSubmodelElementCollection("CarbonFootprint",
                                                "ProductCarbonFootprint", submodelElementCollectionIdShort);

                        }

                        environmentService.updateReferenceElement(ReferableAssetID[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_REFERENCE,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFCalculationMethod[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_CALCULATION_METHOD,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFCO2eq[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCFCO2EQ, submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFQuantityOfMeasureForCalculation[i],
                                        "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFReferenceValueForCalculation[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_REFERENCE_VALUE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFLiveCyclePhase[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFDescription[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_DESCRIPTION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFHandoverStreet[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverNumber[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverCity[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverZIP[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverCountry[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverLatitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                        environmentService.updateProperty(PCFHandoverLongitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, submodelElementCollectionIdShort,
                                        "PCFGoodsAddressHandover");
                }

                /* TRANSPORT CARBON FOOTPRINT */
                for (int i = 0; i < TCFCalculationMethod.length; i++) {
                        String submodelElementCollectionIdShort = "TransportCarbonFootprint";

                        if (i > 0) {
                                submodelElementCollectionIdShort += "_"
                                                + (i+1);
                                environmentService.duplicateSubmodelElementCollection("CarbonFootprint",
                                                "TransportCarbonFootprint", submodelElementCollectionIdShort);
                        }

                        
                        environmentService.updateProperty(TCFCalculationMethod[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_CALCULATION_METHOD,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFCO2eq[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.TCFCO2EQ, submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFReferenceValueForCalculation[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFQuantityOfMeasureForCalculation[i],
                                        "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFProcessesForGreenhouseGasEmissionInATransportService[i],
                                        "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_PROCESSES_FOR_GREENHOUSE_GAS_EMISSION_IN_A_TRANSPORT_SERVICE,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFTakeoverStreet[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverNumber[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverCity[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverZIP[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverCountry[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverLatitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");
                        environmentService.updateProperty(TCFTakeoverLongitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressTakeover");

                        environmentService.updateProperty(TCFHandoverStreet[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.STREET, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverNumber[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.HOUSENUMBER, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverCity[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.CITYTOWN, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverZIP[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.ZIPCODE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverCountry[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.COUNTRY, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverLatitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LATITUDE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");
                        environmentService.updateProperty(TCFHandoverLongitude[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.LONGITUDE, submodelElementCollectionIdShort,
                                        "TCFGoodsTransportAddressHandover");

                }

                environmentServices.add(environmentService);
                serializeEnvironment(environmentService);

                return "redirect:/aas/overview";
        }

        @PostMapping("/aas/export")
        public void exportAAS(@RequestParam("selectedItems") List<String> selectedItems,
                        @RequestParam("exportFormat") String exportFormat,
                        HttpServletResponse response) {

                List<EnvironmentService> selectedEnvironmentServices = environmentServices.stream()
                                .filter(envServ -> selectedItems.contains(envServ.getAssetID()))
                                .collect(Collectors.toList());

                DataType exportDataType = getDataTypeByString(exportFormat);

                if (selectedEnvironmentServices.size() == 1) {

                        DownloadService.downloadFile(
                                        Constants.OUTPUT_DIRECTORY + "/"
                                                        + selectedEnvironmentServices.get(0).getAssetIDShort(),
                                        selectedEnvironmentServices.get(0).getAssetIDShort(), exportDataType, response);

                } else if (selectedEnvironmentServices.size() > 1) {

                        List<String> fileNames = selectedEnvironmentServices.stream()
                                        .map(EnvironmentService::getAssetIDShort)
                                        .collect(Collectors.toList());

                        DownloadService.downloadFiles(
                                        Constants.OUTPUT_DIRECTORY,
                                        fileNames, exportDataType, response);

                }

        }

        // aas/download?id=
        @GetMapping("/aas/download")
        public ResponseEntity<String> downloadAAS(@RequestParam("id") String id,
                        @RequestParam("format") String exportFormat,
                        HttpServletResponse response) {
                if (!existingFilesLoaded)
                        loadEnvironments();
                try {
                        EnvironmentService foundEnvironmentService = environmentServices.stream()
                                        .filter(environmentService -> environmentService.getAssetID().equals(id))
                                        .findFirst()
                                        .orElse(null);

                        if (foundEnvironmentService == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid AssetID!");
                        }

                        DataType exportDataType = getDataTypeByString(exportFormat);

                        if (exportDataType == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Datatype!");
                        }

                        DownloadService.downloadFile(
                                        Constants.OUTPUT_DIRECTORY + "/" + foundEnvironmentService.getAssetIDShort(),
                                        foundEnvironmentService.getAssetIDShort(), exportDataType, response);

                        return ResponseEntity.status(HttpStatus.OK).body("Download successful!");
                } catch (Exception ex) {
                        ex.printStackTrace();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Downlaod Error!");
                }
        }

        private void loadEnvironments() {

                String directoryPath = Constants.OUTPUT_DIRECTORY;

                try {

                        List<Path> jsonFiles = Files.walk(Paths.get(directoryPath), 2)
                                        .filter(path -> path.toString().endsWith(".json"))
                                        .collect(Collectors.toList());

                        jsonFiles.stream()
                                        .map(Path::toString)
                                        .forEach(filename -> {
                                                environmentServices.add(jsonReaderRepository.read(new File(filename)));
                                        });

                } catch (IOException ex) {
                        ex.printStackTrace();
                }

                existingFilesLoaded = true;
        }

        private void serializeEnvironment(EnvironmentService environmentService) {
                DataRepository dataRepository = new AASXDataRepository();
                DataType dataType = DataType.AASX;

                for (int i = 0; i < 2; i++) {

                        String assetIDshort = environmentService.getAssetIDShort();
                        String fileName = assetIDshort
                                        + (dataType == DataType.AASX ? ".aasx" : ".json");

                        dataRepository.write(environmentService, fileName);

                        dataType = DataType.JSON;
                        dataRepository = new JsonDataRepository();

                }
        }

        private void deleteEnvironment(EnvironmentService environmentService) {
                File folderToDelete = new File(Constants.OUTPUT_DIRECTORY + "/" + environmentService.getAssetIDShort());
                File[] files = folderToDelete.listFiles();
                for (File file : files) {
                        file.delete();
                }
                folderToDelete.delete();
        }

        private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
                File file = new File(multipartFile.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(multipartFile.getBytes());
                }
                return file;
        }

        private DataType getDataTypeByString(String format) {
                if (format.equals("json")) {
                        return DataType.JSON;
                } else if (format.equals("aasx")) {
                        return DataType.AASX;
                }
                return null;
        }

        private String getAssetIdShortByAssetId(String assetId) {

                EnvironmentService relatedService = environmentServices.stream()
                                .filter(envService -> envService.getAssetID().equals(assetId))
                                .findFirst()
                                .orElse(null);

                return relatedService != null ? relatedService.getAssetIDShort() : assetId;
        }

        private void sendAlert(String message, HttpServletResponse response) {

                String script = "<script>alert('" + message + "');</script>";
                response.setContentType("text/html");
                try {
                        response.getWriter().println(script);
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

}
