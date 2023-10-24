package com.softwareag.app.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.softwareag.app.data.SubmodelType;
import com.softwareag.app.service.EnvironmentService;

@Controller
public class WebController {

        private DataRepository currentDataRepository;
        private DataType currenDataType;
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
                return "aas_form";
        }

        @PostMapping("/aas/submission")
        public String submission(
                        // parameter Asset Administration Shell
                        @RequestParam("assetIDshort") String assetIDshort,
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
                        @RequestParam("PCFCalculationMethod") String PCFCalculationMethod,
                        @RequestParam("PCFCO2eq") double PCFCO2eq,
                        @RequestParam("PCFQuantityOfMeasureForCalculation") double PCFQuantityOfMeasureForCalculation,
                        @RequestParam("PCFLiveCyclePhase") String PCFLiveCyclePhase,

                        @RequestParam("TCFCalculationMethod") String TCFCalculationMethod,
                        @RequestParam("TCFCO2eq") double TCFCO2eq,
                        @RequestParam("TCFReferenceValueForCalculation") String TCFReferenceValueForCalculation,
                        @RequestParam("TCFQuantityOfMeasureForCalculation") String TCFQuantityOfMeasureForCalculation) {
                currentDataRepository = App.dataRepositoryController.getCurrenDataRepository();
                currenDataType = App.dataRepositoryController.getCurrentDataType();

                EnvironmentService envServ = currentDataRepository
                                .read("FullAASTemplate" + (currenDataType == DataType.AASX ? ".aasx" : ".json"));

                envServ.updateAssetIDShort(assetIDshort);
                envServ.updateAssetID(assetID);

                /* Product Carbon Footprint */
                envServ.updateProperty(PCFCalculationMethod, SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.PCF_CALCULATION_METHOD,
                                SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                envServ.updateProperty(Double.toString(PCFCO2eq), SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.PCFCO2EQ,
                                SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                envServ.updateProperty(Double.toString(PCFQuantityOfMeasureForCalculation),
                                SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.PCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);
                envServ.updateProperty(PCFLiveCyclePhase, SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.PCF_LIVE_CYCLE_PHASE,
                                SubmodelElementCollectionType.PRODUCT_CARBON_FOOTPRINT);

                /* Transport Carbon Footprint */
                envServ.updateProperty(TCFCalculationMethod, SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.TCF_CALCULATION_METHOD,
                                SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                envServ.updateProperty(Double.toString(TCFCO2eq), SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.TCFCO2EQ,
                                SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                envServ.updateProperty(TCFReferenceValueForCalculation, SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.TCF_REFERENCE_VALUE_FOR_CALCULATION,
                                SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);
                envServ.updateProperty(TCFQuantityOfMeasureForCalculation, SubmodelType.CARBON_FOOTPRINT,
                                SubmodelElementPropertyType.TCF_QUANTITY_OF_MEASURE_FOR_CALCULATION,
                                SubmodelElementCollectionType.TRANSPORT_CARBON_FOOTPRINT);

                /* Technical Data */
                envServ.updateProperty(ManufacturerOrderCode, SubmodelType.TECHNICAL_DATA,
                                SubmodelElementPropertyType.MANUFACTURER_ORDER_CODE,
                                SubmodelElementCollectionType.GENERAL_INFORMATION);
                // envServ.updateFile(ManufacturerLogo.getPath(), SubmodelType.TECHNICAL_DATA,
                // SubmodelElementPropertyType.MANUFACTURER_LOGO,
                // SubmodelElementCollectionType.GENERAL_INFORMATION);
                // envServ.updateFile(ProductImage.getPath(), SubmodelType.TECHNICAL_DATA,
                // SubmodelElementPropertyType.PRODUCT_IMAGE,
                // SubmodelElementCollectionType.GENERAL_INFORMATION);

                /* Nameplate */
                envServ.updateProperty(URIOfTheProduct, SubmodelType.NAMEPLATE,
                                SubmodelElementPropertyType.URI_OF_THE_PRODUCT);
                envServ.updateMultilanguageProperty(ManufacturerName, SubmodelType.NAMEPLATE,
                                SubmodelElementPropertyType.MANUFACTURER_NAME);
                envServ.updateProperty(Double.toString(SerialNumber), SubmodelType.NAMEPLATE,
                                SubmodelElementPropertyType.SERIAL_NUMBER);
                envServ.updateProperty(YearOfConstruction, SubmodelType.NAMEPLATE,
                                SubmodelElementPropertyType.YEAR_OF_CONSTRUCTION);
                envServ.updateProperty(DateOfManufacture, SubmodelType.NAMEPLATE,
                                SubmodelElementPropertyType.DATE_OF_MANUFACTURE);

                environmentServices.add(envServ);
                return "redirect:/aas/overview";
        }

        @PostMapping("/aas/export")
        public String exportAAS(@RequestParam("selectedItems") List<String> selectedItems, @RequestParam("exportFormat") String exportFormat) {
                System.out.println(selectedItems);
                System.out.println(exportFormat);

                currentDataRepository = App.dataRepositoryController.getCurrenDataRepository();
                currenDataType = App.dataRepositoryController.getCurrentDataType();
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
