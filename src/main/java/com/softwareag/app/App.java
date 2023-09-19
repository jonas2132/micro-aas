package com.softwareag.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.data.JsonDataRepository;
import com.softwareag.app.service.EnvironmentService;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        
        SpringApplication.run(App.class, args);

        DataRepository dataRepository = new AASXDataRepository();
        Environment env = dataRepository.read("02_Bosch_v.03.aasx");

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
