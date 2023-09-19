package com.softwareag.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.softwareag.app.controller.socketapi.Observer;
import com.softwareag.app.controller.socketapi.ObservableSubject;
import com.softwareag.app.controller.socketapi.APIObserver;

public class APIController extends ObservableSubject {


    private StringBuilder actualString = new StringBuilder();
    

    public void startAPI(){
        int port = 8080; // Desired port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        OutputStream outputStream = clientSocket.getOutputStream()) {

                    String inputLine;

                    while ((inputLine = reader.readLine()) != null) {
                        actualString.append(inputLine);

                    }
                    notifyObservers(actualString.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
