package com.example.jpa.controller;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {

    //@GetMapping
    // public String sayHello() {
    //     return "Hello and Welcome to the app application. You can create a new Note by making a POST request to /api/{table} endpoint.";
    // }   
    public String index() {
        return "index.html";
    }
}
