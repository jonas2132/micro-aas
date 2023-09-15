package com.softwareag.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.softwareag.app.service.CO2Calculation.CO2Emission;


@Controller
public class CO2InputController {

    @GetMapping("/form")
    public String showForm(Model model) {
        // You can add code here to populate the model with initial values
        return "emission_form";
    }

    @PostMapping("/calculate")
    public String calculateCO2Emissions(CO2Emission co2Emission) {
        // Process the submitted form data and update your CO2Emission instance
        // You can access the submitted values through the 'co2Emission' parameter
        return "redirect:/form";
    }
    
}
