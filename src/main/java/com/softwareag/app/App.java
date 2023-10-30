package com.softwareag.app;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.controller.DataRepositoryController;
import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.DataType;
import com.softwareag.app.data.SubmodelElementCollectionType;
import com.softwareag.app.data.SubmodelElementPropertyType;
import com.softwareag.app.service.AASBuilder;
import com.softwareag.app.service.EnvironmentService;
import com.softwareag.app.utils.AASSimple;


@SpringBootApplication
public class App {

    public static DataRepositoryController dataRepositoryController;

    public static void main(String[] args) {
        
        dataRepositoryController = new DataRepositoryController(DataType.JSON);
        EnvironmentService envService = dataRepositoryController.getCurrenDataRepository().read("FullAASTemplate.json");

        envService.duplicateSubmodel("CarbonFootprint", "Carbon_2");
        envService.addCustomReferenceProperty("Carbon_2");

        envService.updateProperty("test", "Carbon_2", SubmodelElementPropertyType.REFERENCE_PROPERTY);
        
        dataRepositoryController.getCurrenDataRepository().write(envService, "test.json");

        
        SpringApplication.run(App.class, args);

        /*EnvironmentService envServ = new EnvironmentService(env);
        envServ.getSubmodels().forEach(model -> {
            System.out.println(model.getId());
        }); */

        //envServ.updatePCFCO2eq("test");

      //  DataRepository dataJsonRepository = new JsonDataRepository();
      //  dataJsonRepository.write(env, "CarbonFootprint_v.03_TST.json");

    //  dataRepository.write(env, "CarbonFootprint_v.03_TST.aasx");

       /* ObservableSubject api = new APIController();
        Observer apoObserver = new APIObserver();
        api.addObserver(apoObserver);
        ((APIController) api).startAPI(); */
        

    }

    
}
