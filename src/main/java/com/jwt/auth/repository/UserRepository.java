package com.jwt.auth.repository;

import com.jwt.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email); // this is a custom method that will be used in the anonymous class implementation of UserDetailsService in ApplicationConfigurationuration.java
}
