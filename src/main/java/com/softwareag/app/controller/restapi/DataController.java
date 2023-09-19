package com.softwareag.app.controller.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    
    @PostMapping("/sendEmissions")
    public void sendEmissionData(@RequestBody User data) {
        System.out.println("Received data: " + data.getName());
    }

    /* @PostMapping("/sendEmissions")
    public void sendEmissionData(@RequestBody String data) {
        System.out.println("Received data: " + data);
    } */

}
