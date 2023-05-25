package com.jwt.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Management", description = "Management Endpoints")
public class ManagementController {

    @Operation(
            summary = "GET endpoint for Management",
            description = "This endpoint is used to GET Management Data"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    public String getManagement(){
        return "Management - GET";
    }

    @Operation(
            summary = "POST endpoint for Management",
            description = "This endpoint is used to POST Management Data"
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    public String postManagement(){
        return "Management - POST";
    }

    @Operation(
            summary = "PUT endpoint for Management",
            description = "This endpoint is used to PUT Management Data"
    )
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public String putManagement(){
        return "Management - PUT";
    }

    @Operation(
            summary = "DELETE endpoint for Management",
            description = "This endpoint is used to DELETE Management Data"
    )
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'management:delete')")
    public String deleteManagement(){
        return "Management - DELETE";
    }
    
}
