package com.softwareag.app.service.CO2Calculation;

import java.util.Map;

//Factory Pattern
public class CalculationFactory {

    private CO2Emission calculator;
    // This method creates and returns an instance of a specific CO2Emission
    // calculation method
    public CalculationFactory(CalculationMethod calculationMethod) {
        switch (calculationMethod) {
            case ENERGY_CONSUMPTION_PRODUCTION_SITE:
                calculator = new EnergyConsumptionProductionSite();
                break;
            case MACHINE_ENERGY_CONSUMPTION:
                calculator = new MachineEnergyConsumption();
                break;
            case PRODUCTION_EMISSIONS:
                calculator = new ProductionEmissions();
                break;
            case WASTE_RELATED_EMISSIONS:
                calculator = new WasteRelatedEmissions();
                break;
            default:
                throw new IllegalArgumentException("Calculation method is not provided");
        }
    }

    public double calculate(Map<String, Double> data) {
        return calculator.calculate(data);
    }


}
