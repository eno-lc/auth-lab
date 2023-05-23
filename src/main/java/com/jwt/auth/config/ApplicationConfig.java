package com.jwt.auth.config;

import com.jwt.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() { // this is an anonymous class implementation of UserDetailsService
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception { // we use authentication manager to authenticate the user
        return configuration.getAuthenticationManager(); // we get the authentication manager from the configuration
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){ // we use authentication provider to authenticate the user
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // we use Dao authentication provider to authenticate the user
        authenticationProvider.setUserDetailsService(userDetailsService()); // we set the user details service
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // we set the password encoder

        return authenticationProvider; // we return the authentication provider
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // we use BCrypt password encoder to encode the password
}
