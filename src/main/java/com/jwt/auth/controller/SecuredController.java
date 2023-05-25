package com.jwt.auth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secured")
public class SecuredController {

    @GetMapping
    @Hidden
    public ResponseEntity<String> secured(){
        return ResponseEntity.ok("Secured");
    }

}
