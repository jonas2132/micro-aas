package com.softwareag.app.service.CO2Calculation;

//Factory Pattern
public class CalculationFactory {

    // This method creates and returns an instance of a specific CO2Emission
    // calculation method
    public CO2Emission createCO2EmissionCalculator(CalculationMethod calculationMethod) {
        switch (calculationMethod) {
            case ENERGY_CONSUMPTION_PRODUCTION_SITE:
                return new EnergyConsumptionProductionSite();
            case MACHINE_ENERGY_CONSUMPTION:
                return new MachineEnergyConsumption();
            case PRODUCTION_EMISSIONS:
                return new ProductionEmissions();
            case WASTE_RELATED_EMISSIONS:
                return new WasteRelatedEmissions();
            default:
                throw new IllegalArgumentException("Calculation method is not provided");
        }

    }
}
