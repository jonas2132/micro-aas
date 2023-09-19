package com.softwareag.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softwareag.app.service.CO2Calculation.CalculationFactory;
import com.softwareag.app.service.CO2Calculation.CalculationMethod;

@RestController
public class APIController {
    
    @PostMapping("/sendEmissions")
    public void sendEmissionData(@RequestBody Map<String, Double> data) {
        System.out.println("Received data");
        CalculationFactory calcFactory = new CalculationFactory();
        calcFactory.createCO2EmissionCalculator(CalculationMethod.ENERGY_CONSUMPTION_PRODUCTION_SITE).calculate(data);
    }

    /* @PostMapping("/sendEmissions")
    public void sendEmissionData(@RequestBody String data) {
        System.out.println("Received data: " + data);
    } */

}
