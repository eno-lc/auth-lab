package com.jwt.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/administrator")
@PreAuthorize("hasRole('ADMIN')")
public class AdministratorController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAdministrator(){
        return "Administrator - GET";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String postAdministrator(){
        return "Administrator - POST";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String putAdministrator(){
        return "Administrator - PUT";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String deleteAdministrator(){
        return "Administrator - DELETE";
    }
}
