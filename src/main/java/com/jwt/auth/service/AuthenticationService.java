package com.jwt.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.auth.dto.AuthenticationRequest;
import com.jwt.auth.dto.AuthenticationResponse;
import com.jwt.auth.dto.RegistrationRequest;
import com.jwt.auth.entity.enums.Role;
import com.jwt.auth.entity.Token;
import com.jwt.auth.entity.enums.TokenType;
import com.jwt.auth.entity.User;
import com.jwt.auth.repository.TokenRepository;
import com.jwt.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
                .role(registrationRequest.getRole()) // we are setting the role of the user
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
        revokeAllUserTokens(user); // we are revoking all the tokens of the user

        saveUserToken(user, authenticationResponse); // we are saving the token in the database and fetching the token from authenticationResponse

        return authenticationResponse; // we are returning an AuthenticationResponse object
    }

    public AuthenticationResponse authenticationResponse(User user){ // this method is used in both register() and authenticate() methods
        String jwtToken = jwtService.generateToken(user); // we are generating a JWT token for the user
        var refreshToken = jwtService.generateRefreshToken(user); // we are generating a refresh token for the user
        return AuthenticationResponse.builder() // we are building an AuthenticationResponse object and returning it
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
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
                .token(authenticationResponse.getAccessToken()) // we are setting the token
                .tokenType(TokenType.BEARER) // we are setting the token type
                .revoked(false) // we are setting the revoked value
                .expired(false) // we are setting the expired value
                .build(); // we are building the Token object

        tokenRepository.save(token); // we are saving the token in the database
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // this is the header that contains the jwt token
        final String refreshToken; // this is the refresh token
        final String userEmail; // this is the user email

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){ // if the header is null or does not start with "Bearer "
            return; // and return
        }

        refreshToken = authorizationHeader.substring(7); // if the header is not null and starts with "Bearer " then we extract the jwt token

        userEmail = jwtService.extractUserEmail(refreshToken); // we extract the user email from the jwt token

        if(userEmail != null){ // if the user email is not null and the authentication context is null
            User user = this.userRepository.findUserByEmail(userEmail).orElseThrow(); // we load the user details from the user email
            if(jwtService.isTokenValid(refreshToken, user)){ // if the jwt token is valid and the token is not expired and not revoked
                var accessToken = jwtService.generateToken(user); // we generate a new jwt token
                revokeAllUserTokens(user);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                saveUserToken(user, authResponse);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse); // we write the new jwt token in the response body
            }
        }
    }
}
