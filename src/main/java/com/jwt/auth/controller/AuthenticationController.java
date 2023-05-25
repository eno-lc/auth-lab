package com.jwt.auth.controller;


import com.jwt.auth.dto.AuthenticationRequest;
import com.jwt.auth.dto.AuthenticationResponse;
import com.jwt.auth.dto.RegistrationRequest;
import com.jwt.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "POST endpoint for Registration",
            description = "This endpoint is used to Register"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registrationRequest){
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @Operation(
            summary = "POST endpoint for Login",
            description = "This endpoint is used to Login"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @Operation(
            summary = "POST endpoint for Refresh Token",
            description = "This endpoint is used to generate a Refresh Token"
    )
    @PostMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @Operation(
            summary = "GET endpoint for User",
            description = "This endpoint is used to GET User Data"
    )
    @GetMapping
    public String getAuth(){
        return "User - GET";
    }

}
