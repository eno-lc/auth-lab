package com.jwt.auth.service;

import com.jwt.auth.config.JwtService;
import com.jwt.auth.dto.AuthenticationRequest;
import com.jwt.auth.dto.AuthenticationResponse;
import com.jwt.auth.dto.RegistrationRequest;
import com.jwt.auth.entity.Role;
import com.jwt.auth.entity.Token;
import com.jwt.auth.entity.TokenType;
import com.jwt.auth.entity.User;
import com.jwt.auth.repository.TokenRepository;
import com.jwt.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository; // we use userRepository to save the user in the database
    private final PasswordEncoder passwordEncoder; // we use passwordEncoder to encode the password
    private final JwtService jwtService; // we use jwtService to generate a JWT token
    private final AuthenticationManager authenticationManager; // we use authenticationManager to authenticate a user
    private final TokenRepository tokenRepository; // we use tokenRepository to save the token in the database

    public AuthenticationResponse register(RegistrationRequest registrationRequest) { // this method is used to register a user
        User user = User.builder() // we are building a User object
                .firstName(registrationRequest.getFirstName()) // we are setting the values of the User object
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword())) // we are encoding the password
                .role(Role.USER) // we are setting the role of the user
                .build();

        User savedUser = userRepository.save(user);// we are saving the user in the database

        AuthenticationResponse authenticationResponse = authenticationResponse(user);  // creating an AuthenticationResponse object to fetch the token

        saveUserToken(savedUser, authenticationResponse); // we are saving the token in the database and fetching the token from authenticationResponse

        return authenticationResponse; // we are returning an AuthenticationResponse object
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) { // this method is used to authenticate a user
        authenticationManager.authenticate( // we are authenticating the user using the AuthenticationManager
                new UsernamePasswordAuthenticationToken( // we are creating a UsernamePasswordAuthenticationToken object because we are using username and password for authentication
                        authenticationRequest.getEmail(), // we are passing the email and password of the user
                        authenticationRequest.getPassword()
                )
        );

        User user = userRepository.findUserByEmail(authenticationRequest.getEmail())
                .orElseThrow(); // we are finding the user by email and throwing an exception if the user is not found

        AuthenticationResponse authenticationResponse = authenticationResponse(user); // creating an AuthenticationResponse object to fetch the token

        revokeAllUserTokens(user);

        saveUserToken(user, authenticationResponse); // we are saving the token in the database and fetching the token from authenticationResponse

        return authenticationResponse; // we are returning an AuthenticationResponse object
    }

    public AuthenticationResponse authenticationResponse(User user){ // this method is used in both register() and authenticate() methods
        String jwtToken = jwtService.generateToken(user); // we are generating a JWT token for the user
        return AuthenticationResponse.builder() // we are building an AuthenticationResponse object and returning it
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user){
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId()); // we are fetching all the valid tokens of the user

        if(validUserTokens.isEmpty()){
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User savedUser, AuthenticationResponse authenticationResponse) { // this method is used to save the token in the database
        var token = Token.builder() // we are building a Token object
                .user(savedUser) // we are setting the user
                .token(authenticationResponse.getToken()) // we are setting the token
                .tokenType(TokenType.BEARER) // we are setting the token type
                .revoked(false) // we are setting the revoked value
                .expired(false) // we are setting the expired value
                .build(); // we are building the Token object

        tokenRepository.save(token); // we are saving the token in the database
    }
}
