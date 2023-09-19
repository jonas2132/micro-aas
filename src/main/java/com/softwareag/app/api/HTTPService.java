package com.softwareag.app.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPService {
    
    /*****************************************************
     * Block 1: ServerSocket Port listening Block
     *****************************************************/

    /*****************************************************
     * Block 1: ServerSocket Port listening Block
     *****************************************************/

    public static void start() {
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

}
