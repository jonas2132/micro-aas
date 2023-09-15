package com.softwareag.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.softwareag.app.data.AASXDataRepository;
import com.softwareag.app.data.DataRepository;
import com.softwareag.app.service.EnvironmentService;


@SpringBootApplication
public class App {

    /*****************************************************
     * Block 1: ServerSocket Port listening Block
     *****************************************************/

    public static void startAPI() {
        int port = 8080; // Desired port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        OutputStream outputStream = clientSocket.getOutputStream()) {

                    System.out.println("Received data from API:");
                    String inputLine;

                    while ((inputLine = reader.readLine()) != null) {
                        System.out.println(inputLine);
                    }

                    System.out.println("End of API data");

                    // Send HTTP 200 response back to the client
                    String response = "HTTP/1.1 200 OK\r\n\r\n";
                    outputStream.write(response.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        DataRepository dataRepository = new AASXDataRepository();
        Environment env = dataRepository.read();

        EnvironmentService envServ = new EnvironmentService(env);
        envServ.getSubmodels().forEach(model -> {
            System.out.println(model.getId());
        });

        envServ.updatePCFCO2eq("a");


   //     startAPI();

    }

    
}
