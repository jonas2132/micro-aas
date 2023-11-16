package com.softwareag.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class WebController {

        private DataRepository jsonReaderRepository = new JsonDataRepository();
        private List<EnvironmentService> environmentServices = new ArrayList<>();
        private boolean existingFilesLoaded = false;

        @GetMapping("/welcome")
        public String welcomeView(Model model) {
                model.addAttribute("pageTitle", "AAS Builder");
                if (!existingFilesLoaded)
                        importEnvironments();
                return "welcome"; // This corresponds to a view named "view.html" in your templates folder
        }

        @GetMapping("/aas/overview")
        public String showOverview(Model model) {
                model.addAttribute("pageTitle", "AAS Overview");
                model.addAttribute("environmentServices", environmentServices);
                if (!existingFilesLoaded)
                        importEnvironments();
                return "overview";
        }

        @PostMapping("/aas/overview/import")
        public ResponseEntity<String> importAAS(Model model, @RequestParam("file") MultipartFile file) {
                if (!file.isEmpty()) {
                        try {

                                String originalFilename = file.getOriginalFilename();
                                String filePath = Constants.RESOURCE_DIRECTORY + "/" + originalFilename;
                                File dest = new File(filePath);
                                file.transferTo(dest);

                                EnvironmentService environmentService = jsonReaderRepository.read(originalFilename);
                                environmentServices.add(environmentService);
                                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
                        } catch (Exception e) {
                                // Handle exceptions if any
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
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
                for (EnvironmentService serv : environmentServices) {
                        environmentServicesIDs.add(serv.getAssetID());
                }

                String dataArrayJson;
                try {
                        dataArrayJson = objectMapper.writeValueAsString(environmentServicesIDs);
                        model.addAttribute("dataArray", dataArrayJson);
                } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return "aas_form";
        }

        @PostMapping("/aas/submission")
        public String submission(
                        // parameter Asset Administration Shell
                        @RequestParam("assetIDshort") String assetIDshort, // includes actual AssetIDShort of AAS and
                                                                           // CarbonFootprint Submodel assetIDshorts
                        @RequestParam("assetID") String assetID,

                        // parameter Nameplate
                        @RequestParam("URIOfTheProduct") String URIOfTheProduct,
                        @RequestParam("ManufacturerName") String ManufacturerName,
                        @RequestParam("SerialNumber") double SerialNumber,
                        @RequestParam("YearOfConstruction") String YearOfConstruction,
                        @RequestParam("DateOfManufacture") String DateOfManufacture,
                        // parameter Technical Data
                        @RequestParam("ManufacturerOrderCode") String ManufacturerOrderCode,
                        // @RequestParam("ManufacturerLogo") File ManufacturerLogo,
                        // @RequestParam("ProductImage") File ProductImage,

                        // Parameter Carbon Footprint

                        @RequestParam(value = "ReferableAssetID", defaultValue = " ") String[] ReferableAssetID,

                        @RequestParam("PCFCalculationMethod") String[] PCFCalculationMethod,
                        @RequestParam("PCFCO2eq") double[] PCFCO2eq,
                        @RequestParam("PCFQuantityOfMeasureForCalculation") double[] PCFQuantityOfMeasureForCalculation,
                        @RequestParam("PCFReferenceValueForCalculation") String[] PCFReferenceValueForCalculation,
                        @RequestParam("PCFLiveCyclePhase") String[] PCFLiveCyclePhase,
                        @RequestParam(value = "PCFDescription", defaultValue = " ") String[] PCFDescription,

                        @RequestParam("TCFCalculationMethod") String[] TCFCalculationMethod,
                        @RequestParam("TCFCO2eq") double[] TCFCO2eq,
                        @RequestParam("TCFReferenceValueForCalculation") String[] TCFReferenceValueForCalculation,
                        @RequestParam("TCFQuantityOfMeasureForCalculation") double[] TCFQuantityOfMeasureForCalculation) {

                EnvironmentService environmentService = jsonReaderRepository
                                .read(Constants.RESOURCE_DIRECTORY + "/" + "FullAASTemplate_custom.json");

                environmentService.updateAssetIDShort(assetIDshort);
                environmentService.updateAssetID(assetID);

                /* NAMEPLATE */
                environmentService.updateProperty(URIOfTheProduct, "Nameplate",
                                SubmodelElementPropertyType.URI_OF_THE_PRODUCT);
                environmentService.updateMultilanguageProperty(ManufacturerName, "Nameplate",
                                SubmodelElementPropertyType.MANUFACTURER_NAME);
                environmentService.updateProperty(Double.toString(SerialNumber), "Nameplate",
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
                                submodelElementCollectionIdShort += "_"
                                                + getAssetIdShortByAssetId(ReferableAssetID[i]);
                                environmentService.duplicateSubmodelElementCollection("CarbonFootprint",
                                                "ProductCarbonFootprint", submodelElementCollectionIdShort);

                        }

                        environmentService.updateReferenceElement(ReferableAssetID[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_REFERENCE,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFCalculationMethod[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_CALCULATION_METHOD,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(Double.toString(PCFCO2eq[i]), "CarbonFootprint",
                                        SubmodelElementPropertyType.PCFCO2EQ, submodelElementCollectionIdShort);
                        environmentService.updateProperty(Double.toString(PCFQuantityOfMeasureForCalculation[i]),
                                        "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFLiveCyclePhase[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(PCFDescription[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.PCF_ASSET_DESCRIPTION,
                                        submodelElementCollectionIdShort);

                }

                /* TRANSPORT CARBON FOOTPRINT */
                for (int i = 0; i < TCFCalculationMethod.length; i++) {
                        String submodelElementCollectionIdShort = "TransportCarbonFootprint";

                        if (i > 0) {
                                submodelElementCollectionIdShort += "_"
                                                + getAssetIdShortByAssetId(ReferableAssetID[i]);
                                environmentService.duplicateSubmodelElementCollection("CarbonFootprint",
                                                "TransportCarbonFootprint", submodelElementCollectionIdShort);
                        }

                        environmentService.updateProperty(TCFCalculationMethod[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_CALCULATION_METHOD,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(Double.toString(TCFCO2eq[i]), "CarbonFootprint",
                                        SubmodelElementPropertyType.TCFCO2EQ, submodelElementCollectionIdShort);
                        environmentService.updateProperty(TCFReferenceValueForCalculation[i], "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                        environmentService.updateProperty(Double.toString(TCFQuantityOfMeasureForCalculation[i]),
                                        "CarbonFootprint",
                                        SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                        submodelElementCollectionIdShort);
                }

                environmentServices.add(environmentService);
                exportEnvironment(environmentService);

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
                                        Constants.OUTPUT_DIRECTORY + "/" + selectedEnvironmentServices.get(0).getAssetIDShort(),
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

        @GetMapping("/aas/download")
        public ResponseEntity<String> downloadAAS(@RequestParam("id") String id,
                        @RequestParam("format") String exportFormat,
                        HttpServletResponse response) {
                try {
                EnvironmentService foundEnvironmentService = environmentServices.stream()
                                .filter(environmentService -> environmentService.getAssetID().equals(id))
                                .findFirst()
                                .orElse(null);

                if (foundEnvironmentService == null) {
                        sendAlert("Invalid AssetID!", response);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid AssetID!");
                }

                DataType exportDataType = getDataTypeByString(exportFormat);

                if (exportDataType == null) {
                        sendAlert("Invalid Datatype!", response);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Datatype!");
                }

                DownloadService.downloadFile(
                                Constants.OUTPUT_DIRECTORY + "/" + foundEnvironmentService.getAssetIDShort(),
                                foundEnvironmentService.getAssetIDShort(), exportDataType, response);

                return ResponseEntity.status(HttpStatus.OK).body("Download successful!");
                }catch(Exception ex) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Downlaod Error!");
                }       
        }

        private void importEnvironments() {

                String directoryPath = Constants.OUTPUT_DIRECTORY;

                try {

                        List<Path> jsonFiles = Files.walk(Paths.get(directoryPath), 2)
                                        .filter(path -> path.toString().endsWith(".json"))
                                        .collect(Collectors.toList());

                        jsonFiles.stream()
                                        .map(Path::toString)
                                        .forEach(filename -> {
                                                environmentServices.add(jsonReaderRepository.read(filename));
                                        });

                } catch (IOException ex) {
                        ex.printStackTrace();
                }

                existingFilesLoaded = true;
        }

        private void exportEnvironment(EnvironmentService environmentService) {
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
