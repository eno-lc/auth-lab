package com.jwt.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/administrator")
public class AdministratorController {

    @GetMapping
    public String getAdministrator(){
        return "Administrator - GET";
    }

    @PostMapping
    public String postAdministrator(){
        return "Administrator - POST";
    }

    @PutMapping
    public String putAdministrator(){
        return "Administrator - PUT";
    }

    @DeleteMapping
    public String deleteAdministrator(){
        return "Administrator - DELETE";
    }
}
