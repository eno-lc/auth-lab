package com.jwt.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class ManagementController {


    @GetMapping
@PreAuthorize("hasAnyAuthority('admin:read', 'management:read')")
    public String getManagement(){
        return "Management - GET";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'management:create')")
    public String postManagement(){
        return "Management - POST";
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:update', 'management:update')")
    public String putManagement(){
        return "Management - PUT";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'management:delete')")
    public String deleteManagement(){
        return "Management - DELETE";
    }
    
}
