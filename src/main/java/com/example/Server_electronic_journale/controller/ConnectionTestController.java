package com.example.Server_electronic_journale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionTestController {

    @GetMapping("/test")
    public String testConnection() {
        return "Connection successful!";
    }
}
