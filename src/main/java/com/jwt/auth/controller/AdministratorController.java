package com.jwt.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/administrator")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administrator", description = "Administrator Endpoints")
public class AdministratorController {

    @Operation(
            summary = "GET endpoint for Administrator",
            description = "This endpoint is used to GET Administrator Data"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAdministrator(){
        return "Administrator - GET";
    }

    @Operation(
            summary = "POST endpoint for Administrator",
            description = "This endpoint is used to POST Administrator Data"
    )
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String postAdministrator(){
        return "Administrator - POST";
    }

    @Operation(
            summary = "PUT endpoint for Administrator",
            description = "This endpoint is used to PUT Administrator Data"
    )
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String putAdministrator(){
        return "Administrator - PUT";
    }

    @Operation(
            summary = "DELETE endpoint for Administrator",
            description = "This endpoint is used to DELETE Administrator Data"
    )
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String deleteAdministrator(){
        return "Administrator - DELETE";
    }
}
