package com.jwt.auth.repository;

import com.jwt.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
    SELECT t from Token t
    INNER JOIN User u on u.id = t.user.id
    WHERE u.id = ?1
    AND (t.revoked = false OR t.expired = false)
    """)
    List<Token> findAllValidTokensByUserId(Long userId);

    Optional<Token> findByToken(String token);
}
