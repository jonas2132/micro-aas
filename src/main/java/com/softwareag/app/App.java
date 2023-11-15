package com.softwareag.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.controller.DataRepositoryController;
import com.softwareag.app.data.DataType;
import com.softwareag.app.service.EnvironmentService;


@SpringBootApplication
public class App {

    public static DataRepositoryController dataRepositoryController;

    public static void main(String[] args) {
        
        dataRepositoryController = new DataRepositoryController(DataType.JSON);

        EnvironmentService environmentService = dataRepositoryController.getCurrenDataRepository().read("FullAASTemplate.json");
        //environmentService.duplicateSubmodel("CarbonFootprint", "https://admin-shell.io/idta/CarbonFootprint/CarbonFootprint/1/0/CarbonFootprint_Test", "CarbonFootprint_Test");
        environmentService.duplicateSubmodelElementCollection("CarbonFootprint", "ProductCarbonFootprint", "ProductCarbonFootprint_Test");
        //environmentService.addCustomProperty("CarbonFootprint", "Testproperty");
        dataRepositoryController.getCurrenDataRepository().write(environmentService, "AASTest.json");
     
        SpringApplication.run(App.class, args);
        

    }

    
}
