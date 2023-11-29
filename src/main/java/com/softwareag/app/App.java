package com.softwareag.app;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.controller.DataRepositoryController;
import com.softwareag.app.data.DataType;
import com.softwareag.app.service.EnvironmentService;
import com.softwareag.app.utils.Constants;

@SpringBootApplication
public class App {

    public static DataRepositoryController dataRepositoryController;

    public static void main(String[] args) {

        // dataRepositoryController = new DataRepositoryController(DataType.JSON);

      //  EnvironmentService environmentService = dataRepositoryController.getCurrenDataRepository()
          //      .read(new File(Constants.RESOURCE_DIRECTORY + "/" + "FullAASTemplate.json"));

        // environmentService.duplicateSubmodelElementCollection("CarbonFootprint",
        // "ProductCarbonFootprint", "ProductCarbonFootprint_Test");

        // environmentService.addCustomProperty("CarbonFootprint", "Testproperty");

        //dataRepositoryController = new DataRepositoryController(DataType.AASX);
        //dataRepositoryController.getCurrenDataRepository().write(environmentService,
        //        "AASTest.aasx");

        SpringApplication.run(App.class, args);

    }

}
