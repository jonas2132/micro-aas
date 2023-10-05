package com.softwareag.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WebController {

    @GetMapping("/welcome")
    public String welcomeView(Model model) {
        // Add any necessary data to the model for rendering the view
        model.addAttribute("pageTitle", "AAS Builder");
        return "welcome"; // This corresponds to a view named "view.html" in your templates folder
    }


    @GetMapping("/aas/overview")
    public String showOverview(Model model){
        model.addAttribute("pageTitle", "AAS Overview");
        return "overview";
    }

    @GetMapping("/aas/form")
    public String showForm(Model model){
        model.addAttribute("pageTitle", "AAS Configurator");
        return "aas_form";
    }



    @PostMapping("/aas/submission")
    public String submussion(
        @RequestParam("assetIDshort") String assetIDshort,
        @RequestParam("assetID") String assetID,
        @RequestParam("PCFCalculationMethod") String PCFCalculationMethod,
        @RequestParam("PCFCO2eq") double PCFCO2eq,
        @RequestParam("PCFQuantityOfMeasureForCalculation") double PCFQuantityOfMeasureForCalculation,
        @RequestParam("PCFLiveCyclePhase") String PCFLiveCyclePhase,
        @RequestParam("TCFCalculationMethod") String TCFCalculationMethod,
        @RequestParam("TCFCO2eq") double TCFCO2eq,
        @RequestParam("TCFReferenceValueForCalculation") String TCFReferenceValueForCalculation,
        @RequestParam("TCFQuantityOfMeasureForCalculation") String TCFQuantityOfMeasureForCalculation,
        @RequestParam("TCFLiveCyclePhase") String TCFLiveCyclePhase

    ){
        System.out.println(assetIDshort);
        System.out.println(assetID);
        System.out.println(PCFCalculationMethod);
        System.out.println(PCFQuantityOfMeasureForCalculation);
        System.out.println(PCFLiveCyclePhase);
        System.out.println(TCFCalculationMethod);
        System.out.println(TCFCO2eq);
        System.out.println(TCFReferenceValueForCalculation);
        System.out.println(TCFQuantityOfMeasureForCalculation);
        System.out.println(TCFLiveCyclePhase);
        return "redirect:/aas/overview";
    }








}
