package com.softwareag.app.controller.restapi;

public class User {
    private String name;
    private int age;

    public User() {
        // Standardkonstruktor ohne Argumente
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter und Setter für name und age
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

