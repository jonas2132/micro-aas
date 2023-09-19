package com.softwareag.app.service.CO2Calculation;

import java.util.Map;

public class MachineEnergyConsumption implements CO2Emission{

    private double runtime;
    private double avgConsumtion;
    private double co2Equivalent;


    @Override
    public double calculate(Map<String, Double> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculate'");
    }
    
}
