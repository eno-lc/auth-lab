package com.jwt.auth.config;

import com.jwt.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.jwt.auth.entity.enums.Permission.*;
import static com.jwt.auth.entity.enums.Role.ADMIN;
import static com.jwt.auth.entity.enums.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // we use JWT authentication filter to authenticate the user
    private final AuthenticationProvider authenticationProvider; // we use authentication provider to authenticate the user
    private final LogoutHandler logoutHandler; // we use logout service to logout the user

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // we use security filter chain to configure the security
        http
                .csrf().disable() // we disable csrf because we are using JW, so we don't need csrf because we are not using cookies
                .authorizeHttpRequests() // we authorize the http requests
                .requestMatchers("/api/v1/auth/**") // we authorize the requests that start with /api/v1/auth
                .permitAll() // we permit all the requests that start with /api/v1/auth

                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name()) // we authorize the requests that start with /api/v1/management and the user must have the role ADMIN or MANAGER
                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name()) // we authorize the requests that start with /api/v1/management and the user must have the authority ADMIN_READ or MANAGER_READ
                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name()) // we authorize the requests that start with /api/v1/management and the user must have the authority ADMIN_CREATE or MANAGER_CREATE
                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name()) // we authorize the requests that start with /api/v1/management and the user must have the authority ADMIN_UPDATE or MANAGER_UPDATE
                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name()) // we authorize the requests that start with /api/v1/management and the user must have the authority ADMIN_DELETE or MANAGER_DELETE

                .requestMatchers("/api/v1/administrator/**").hasRole(ADMIN.name()) // we authorize the requests that start with /api/v1/administrator and the user must have the role ADMIN
                .requestMatchers(GET, "/api/v1/administrator/**").hasAuthority(ADMIN_READ.name()) // we authorize the requests that start with /api/v1/administrator and the user must have the authority ADMIN_READ
                .requestMatchers(POST, "/api/v1/administrator/**").hasAuthority(ADMIN_CREATE.name()) // we authorize the requests that start with /api/v1/administrator and the user must have the authority ADMIN_CREATE
                .requestMatchers(PUT, "/api/v1/administrator/**").hasAuthority(ADMIN_UPDATE.name()) // we authorize the requests that start with /api/v1/administrator and the user must have the authority ADMIN_UPDATE
                .requestMatchers(DELETE, "/api/v1/administrator/**").hasAuthority(ADMIN_DELETE.name()) // we authorize the requests that start with /api/v1/administrator and the user must have the authority ADMIN_DELETE

                .anyRequest() // any other request
                .authenticated() // must be authenticated
                .and()
                .sessionManagement() // we manage the session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // we use stateless session because we are using JWT and we don't need to store the session because we are not using cookies
                .and()
                .authenticationProvider(authenticationProvider) // we use authentication provider to authenticate the user
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // we add the JWT authentication filter before the username password authentication filter because we want to authenticate the user before the username password authentication filter
                .logout() // we logout
                .logoutUrl("/api/v1/auth/logout") // we log out from the url /api/v1/auth/logout
                .addLogoutHandler(logoutHandler) // we add logout handler the work that we want to do when the user logout
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()); // we clear the security context when the user logout
        return http.build();
    }

}
