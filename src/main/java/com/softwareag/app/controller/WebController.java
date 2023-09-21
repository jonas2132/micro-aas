package com.softwareag.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {

    @GetMapping("/view")
    public String displayView(Model model) {
        // Add any necessary data to the model for rendering the view
        String message = "This is a message I'm printing!";
        model.addAttribute("message", message);
        return "emission_form"; // This corresponds to a view named "view.html" in your templates folder
    }
        
}
