package com.jwt.auth.config;

import com.jwt.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // we use JWT authentication filter to authenticate the user
    private final AuthenticationProvider authenticationProvider; // we use authentication provider to authenticate the user
    private final LogoutHandler logoutHandler; // we use logout service to logout the user

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // we use security filter chain to configure the security
        http
                .csrf().disable() // we disable csrf because we are using JW, so we don't need csrf because we are not using cookies
                .authorizeHttpRequests() // we authorize the http requests
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                ) // we authorize the requests that start with /api/v1/auth
                .permitAll() // we permit all the requests that start with /api/v1/auth
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
