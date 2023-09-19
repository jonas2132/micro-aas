package com.softwareag.app.controller.socketapi;

import com.softwareag.app.controller.socketapi.Observer;

public class APIObserver implements Observer {


    @Override
    public void update(String message) {
        System.out.println("Ich bin " + getClass() + " und sage " + message);
    }


    
}
