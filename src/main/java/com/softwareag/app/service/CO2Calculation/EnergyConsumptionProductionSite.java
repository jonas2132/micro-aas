package com.softwareag.app.service.CO2Calculation;

import java.util.Map;

public class EnergyConsumptionProductionSite implements CO2Emission {

    private double productionSiteEnergyConsumption;
    private double co2Equivalent;
    private double percentageOfRenewableEnergy;
    private double producedAssets;


    
    public double calculate(Map<String, Double> data) {
        initData(data);
        double result = 0.0;
        result = (productionSiteEnergyConsumption - percentageOfRenewableEnergy*productionSiteEnergyConsumption) * co2Equivalent / producedAssets;
        System.out.println("Average Energy Consumption of the asset in the certain production site: " + result);
        return 0.0;
    }

    public void initData(Map<String, Double> data){
        this.productionSiteEnergyConsumption = data.get("productionSiteEnergyConsumtion");
        this.co2Equivalent = data.get("co2Equivalent");
        this.percentageOfRenewableEnergy = data.get("percentageOfRenewableEnergy");
        this.producedAssets = data.get("producedAssets");
    }
    
    
    
}
