package com.jwt.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest { // we will use this class to receive the registration request from the client
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
