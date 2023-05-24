package com.jwt.auth.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jwt.auth.entity.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(Set.of()),
    ADMIN(Set.of(ADMIN_READ, ADMIN_CREATE, ADMIN_UPDATE, ADMIN_DELETE, MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE)),
    MANAGER(Set.of(MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE));

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList();


        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
