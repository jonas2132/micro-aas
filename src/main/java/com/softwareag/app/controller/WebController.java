package com.softwareag.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;

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
import com.softwareag.app.data.SubmodelElementCollectionType;
import com.softwareag.app.data.SubmodelElementPropertyType;
import com.softwareag.app.service.EnvironmentService;

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
                List<String> numbers = new ArrayList<>();
                numbers.add("eins");
                numbers.add("zwei");
                numbers.add("drei");
                numbers.add("vier");
                numbers.add("fünf");

                String numbersJson;
                try {
                        numbersJson = objectMapper.writeValueAsString(numbers);
                        model.addAttribute("numbers", numbersJson);
                } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        System.out.println("JsonError");
                        e.printStackTrace();
                }

                return "aas_form";
        }

        @PostMapping("/aas/submission")
        public String submission(
                        // parameter Asset Administration Shell
                        @RequestParam("assetIDshort") String[] assetIDshort, // includes actual AssetIDShort of AAS and
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

                        @RequestParam("PCFCalculationMethod") String[] PCFCalculationMethod,
                        @RequestParam("PCFCO2eq") double[] PCFCO2eq,
                        @RequestParam("PCFQuantityOfMeasureForCalculation") double[] PCFQuantityOfMeasureForCalculation,
                        @RequestParam("PCFReferenceValueForCalculation") String[] PCFReferenceValueForCalculation,
                        @RequestParam("PCFLiveCyclePhase") String[] PCFLiveCyclePhase,
                        @RequestParam("PCFDescription") String[] PCFDescription,

                        @RequestParam("TCFCalculationMethod") String[] TCFCalculationMethod,
                        @RequestParam("TCFCO2eq") double[] TCFCO2eq,
                        @RequestParam("TCFReferenceValueForCalculation") String[] TCFReferenceValueForCalculation,
                        @RequestParam("TCFQuantityOfMeasureForCalculation") double[] TCFQuantityOfMeasureForCalculation) {

                System.out.println("PCF Values: ");
                for (String element : PCFCalculationMethod) {
                        System.out.println(element);
                }
                for (double element : PCFCO2eq) {
                        System.out.println(element);
                }
                for (double element : PCFQuantityOfMeasureForCalculation) {
                        System.out.println(element);
                }
                for (String element : PCFReferenceValueForCalculation) {
                        System.out.println(element);
                }
                for (String element : PCFLiveCyclePhase) {
                        System.out.println(element);
                }
                for (String element : PCFDescription) {
                        System.out.println(element);
                }

                System.out.println("TCF Values: ");
                for (String element : TCFCalculationMethod) {
                        System.out.println(element);
                }
                for (double element : TCFCO2eq) {
                        System.out.println(element);
                }
                for (double element : TCFQuantityOfMeasureForCalculation) {
                        System.out.println(element);
                }
                for (String element : TCFReferenceValueForCalculation) {
                        System.out.println(element);
                }

                EnvironmentService environmentService = currentDataRepository.read("FullAASTemplate.json");

                environmentService.updateAssetIDShort(assetIDshort[0]);
                environmentService.updateAssetID(assetID);

                /* Nameplate */
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

                /* Technical Data */
                environmentService.updateProperty(ManufacturerOrderCode, "TechnicalData",
                                SubmodelElementPropertyType.MANUFACTURER_ORDER_CODE,
                                SubmodelElementCollectionType.GENERAL_INFORMATION);

                // for(int i = 0; i<PCFCalculationMethod.length; i++) {
                // String submodelIdShort = "CarbonFootprint";

                // if(i>0) {
                // submodelIdShort += "_" + assetIDshort[i];
                // environmentService.duplicateSubmodel("CarbonFootprint", submodelIdShort);
                // }

                // /* PRODUCT CARBON FOOTPRINT */
                // environmentService.updateProperty(PCFCalculationMethod[i], submodelIdShort,
                // SubmodelElementPropertyType.PCF_CALCULATION_METHOD,
                // SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(Double.toString(PCFCO2eq[i]),
                // submodelIdShort,
                // SubmodelElementPropertyType.PCFCO2EQ,
                // SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(Double.toString(PCFQuantityOfMeasureForCalculation[i]),
                // submodelIdShort,
                // SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                // SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(PCFLiveCyclePhase[i], submodelIdShort,
                // SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE,
                // SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);

                // /* TRANSPORT CARBON FOOTPRINT */
                // environmentService.updateProperty(TCFCalculationMethod[i], submodelIdShort,
                // SubmodelElementPropertyType.TCF_CALCULATION_METHOD,
                // SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(Double.toString(TCFCO2eq[i]),
                // submodelIdShort,
                // SubmodelElementPropertyType.TCFCO2EQ,
                // SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(TCFReferenceValueForCalculation[i],
                // submodelIdShort,
                // SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION,
                // SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                // environmentService.updateProperty(Double.toString(TCFQuantityOfMeasureForCalculation[i]),
                // submodelIdShort,
                // SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                // SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);

                // }

                environmentServices.add(environmentService);

                return "redirect:/aas/overview";
        }

        @PostMapping("/aas/export")
        public String exportAAS(@RequestParam("selectedItems") List<String> selectedItems,
                        @RequestParam("exportFormat") String exportFormat) {
                System.out.println(selectedItems);
                System.out.println(exportFormat);

                environmentServices.stream()
                                .filter(envServ -> selectedItems.contains(envServ.getAssetID()))
                                .forEach(envServ -> {
                                        String assetIDshort = envServ.getAssetIDShort();
                                        currentDataRepository.write(envServ, assetIDshort
                                                        + (currenDataType == DataType.AASX ? ".aasx" : ".json"));
                                });
                return "redirect:/aas/overview";
        }

}
