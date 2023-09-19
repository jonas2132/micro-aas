package com.softwareag.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.controller.APIController;
import com.softwareag.app.controller.socketapi.APIObserver;
import com.softwareag.app.controller.socketapi.ObservableSubject;
import com.softwareag.app.controller.socketapi.Observer;
import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.service.EnvironmentService;


@SpringBootApplication
public class App {

    public static void main(String[] args) {

        /* DataRepository dataRepository = new AASXDataRepository();
        Environment env = dataRepository.read();

        EnvironmentService envServ = new EnvironmentService(env);
        envServ.getSubmodels().forEach(model -> {
            System.out.println(model.getId());
        });

        envServ.updatePCFCO2eq("a"); */


   //     startAPI();

        ObservableSubject api = new APIController();
        Observer apoObserver = new APIObserver();
        api.addObserver(apoObserver);
        ((APIController) api).startAPI();
        

    }

    
}
