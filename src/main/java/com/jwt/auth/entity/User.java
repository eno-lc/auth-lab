package com.jwt.auth.entity;

import com.jwt.auth.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
        name = "users_sequence",
        sequenceName = "users_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "users_sequence",
        strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // this method is used to get the role of the user
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() { // this method is used to get the password of the user
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    } // this method is used to get the username of the user

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // this method is used to check if the account of the user is expired

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // this method is used to check if the account of the user is locked

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // this method is used to check if the credentials of the user is expired

    @Override
    public boolean isEnabled() {
        return true;
    } // this method is used to check if the account of the user is enabled
}
