package com.jwt.auth.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "6B5970337336763979244226452948404D6351655468576D5A7134743777217A";

    public String extractUserEmail(String jwtToken) {
        return null;
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken); // we extract all the claims from the JWT
        return claimsResolver.apply(claims); // claimsResolver.apply() method is used to extract a specific claim from the JWT
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // we decode the secret key from base64 to bytes because the Keys.hmacShaKeyFor() method requires a byte array
        return Keys.hmacShaKeyFor(keyBytes); // Keys.hmacShaKeyFor() method returns a Key object that can be used to sign or verify a JWT
    }
}
