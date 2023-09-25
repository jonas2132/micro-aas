package com.softwareag.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    @GetMapping("/welcome")
    public String welcomeView() {
        // Add any necessary data to the model for rendering the view
        return "welcome_view"; // This corresponds to a view named "view.html" in your templates folder
    }


    @PostMapping("/aas/overview")
    public String showOverview(){
        return "overview";
    
    }







}
