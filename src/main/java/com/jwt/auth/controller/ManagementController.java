package com.jwt.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {


    @GetMapping
    public String getManagement(){
        return "Management - GET";
    }

    @PostMapping
    public String postManagement(){
        return "Management - POST";
    }

    @PutMapping
    public String putManagement(){
        return "Management - PUT";
    }

    @DeleteMapping
    public String deleteManagement(){
        return "Management - DELETE";
    }
    
}
