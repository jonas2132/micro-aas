package com.softwareag.app.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.softwareag.app.App;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.DataType;
import com.softwareag.app.data.SubmodelElementPropertyType;
import com.softwareag.app.service.DownloadService;
import com.softwareag.app.service.EnvironmentService;
import com.softwareag.app.utils.Constants;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class WebController {

        private DataRepository currentDataRepository = App.dataRepositoryController.getCurrenDataRepository();
        private DataType currenDataType = App.dataRepositoryController.getCurrentDataType();
        private List<EnvironmentService> environmentServices = new ArrayList<>();

        @GetMapping("/welcome")
        public String welcomeView(Model model) {
                model.addAttribute("pageTitle", "AAS Builder");
                return "welcome"; // This corresponds to a view named "view.html" in your templates folder
        }

        @GetMapping("/aas/overview")
        public String showOverview(Model model) {
                model.addAttribute("pageTitle", "AAS Overview");
                model.addAttribute("environmentServices", environmentServices);
                return "overview";
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

                EnvironmentService environmentService = currentDataRepository.read("FullAASTemplate_custom.json");

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

                return "redirect:/aas/overview";
        }

        @PostMapping("/aas/export")
        public void exportAAS(@RequestParam("selectedItems") List<String> selectedItems,
                        @RequestParam("exportFormat") String exportFormat,
                        HttpServletResponse response) {

                List<EnvironmentService> selectedEnvironmentServices = environmentServices.stream()
                                .filter(envServ -> selectedItems.contains(envServ.getAssetID()))
                                .collect(Collectors.toList());

                if (selectedEnvironmentServices.size() == 1) {
                        DownloadService.downloadEnvironment(selectedEnvironmentServices.get(0), Constants.OUTPUT_DIRECTORY,
                                        App.dataRepositoryController, response);
                } else if (selectedEnvironmentServices.size() > 1) {
                        DownloadService.downloadEnvironments(selectedEnvironmentServices, Constants.OUTPUT_DIRECTORY,
                                        App.dataRepositoryController, response);
                }
        }

        // @GetMapping("/aas/download")
        // public void downloadAAS(@RequestParam("id") String id,
        //                 @RequestParam("format") String format,
        //                 HttpServletResponse response) {

        //         // Überprüfen Sie hier die ID und das Format auf Gültigkeit, falls erforderlich

        //         String fileName = id + (format.equals("json") ? ".json" : ".aasx");
        //         String filePath = outputDir + "/" + fileName;

        //         response.setContentType("application/octet-stream");
        //         response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        //         try (FileInputStream in = new FileInputStream(filePath);
        //                         OutputStream out = response.getOutputStream()) {

        //                 byte[] buffer = new byte[4096];
        //                 int length;
        //                 while ((length = in.read(buffer)) > 0) {
        //                         out.write(buffer, 0, length);
        //                 }
        //                 out.flush();
        //         } catch (IOException e) {
        //                 e.printStackTrace();
        //         }
        // }

        private void exportEnvironment(EnvironmentService environmentService) {
                
        }

        private String getAssetIdShortByAssetId(String assetId) {

                EnvironmentService relatedService = environmentServices.stream()
                                .filter(envService -> envService.getAssetID().equals(assetId))
                                .findFirst()
                                .orElse(null);

                return relatedService != null ? relatedService.getAssetIDShort() : assetId;
        }

}
