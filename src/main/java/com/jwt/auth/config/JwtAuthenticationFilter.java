package com.jwt.auth.config;

import com.jwt.auth.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization"); // this is the header that contains the jwt token
        final String jwtToken; // this is the jwt token
        final String userEmail; // this is the user email

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){ // if the header is null or does not start with "Bearer "
            filterChain.doFilter(request, response); // then we just pass the request and response to the next filter
            return; // and return
        }

        jwtToken = authorizationHeader.substring(7); // if the header is not null and starts with "Bearer " then we extract the jwt token

        userEmail = jwtService.extractUserEmail(jwtToken); // we extract the user email from the jwt token

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){ // if the user email is not null and the authentication context is null
            UserDetails user = this.userDetailsService.loadUserByUsername(userEmail); // we load the user details from the user email
            if(jwtService.isTokenValid(jwtToken, user)){ // if the jwt token is valid
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // we create an authentication token with the user details which we use to authenticate the user
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // we set the details of the authentication token to the details of the request
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // we set the authentication token to the authentication context
            }
        }
        filterChain.doFilter(request, response); // we pass the request and response to the next filter
    }
}
