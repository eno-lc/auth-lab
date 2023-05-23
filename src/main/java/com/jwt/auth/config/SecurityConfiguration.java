package com.jwt.auth.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // we use JWT authentication filter to authenticate the user
    private final AuthenticationProvider authenticationProvider; // we use authentication provider to authenticate the user

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // we use security filter chain to configure the security
        http
                .csrf().disable() // we disable csrf because we are using JW, so we don't need csrf because we are not using cookies
                .authorizeHttpRequests() // we authorize the http requests
                .requestMatchers("/api/v1/auth/**") // we authorize the requests that start with /api/v1/auth
                .permitAll() // we permit all the requests that start with /api/v1/auth
                .anyRequest() // any other request
                .authenticated() // must be authenticated
                .and()
                .sessionManagement() // we manage the session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // we use stateless session because we are using JWT and we don't need to store the session because we are not using cookies
                .and()
                .authenticationProvider(authenticationProvider) // we use authentication provider to authenticate the user
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // we add the JWT authentication filter before the username password authentication filter because we want to authenticate the user before the username password authentication filter

        return http.build();
    }

}
