package com.jwt.auth.service;

import com.jwt.auth.dto.AuthenticationRequest;
import com.jwt.auth.dto.AuthenticationResponse;
import com.jwt.auth.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return null;
    }
}
