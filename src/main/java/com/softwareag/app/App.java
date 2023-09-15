package com.softwareag.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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




    /*****************************************************
     * Block 6: Start of the Spring Application
     *****************************************************/

    /* SpringApplication.run(App.class, args); */

    public static void main(String[] args) {
        // Environment env = AASXDataRepository.AASXReading();

        // EnvironmentService envServ = new EnvironmentService(env);
        // envServ.updatePCFCO2eq("30");

        /* AASXDataRepository.AASXWriting(env);
        JsonDataRepository.JsonWriting(env); */

        startAPI();

    }

    
}
