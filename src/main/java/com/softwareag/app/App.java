package com.softwareag.app;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        
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
