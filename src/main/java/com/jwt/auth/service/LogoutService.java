package com.jwt.auth.service;

import com.jwt.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authorizationHeader = request.getHeader("Authorization"); // this is the header that contains the jwt token
        final String jwtToken; // this is the jwt token

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){ // if the header is null or does not start with "Bearer "
            return; // and return
        }
        jwtToken = authorizationHeader.substring(7); // if the header is not null and starts with "Bearer " then we extract the jwt token
        var storedToken = tokenRepository.findByToken(jwtToken)
                .orElse(null);

        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
