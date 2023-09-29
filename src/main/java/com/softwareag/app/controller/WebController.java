package com.softwareag.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


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
    public String showOver(Model model){
        model.addAttribute("pageTitle", "AAS Overview");
        return "aas_form";
    }







}
