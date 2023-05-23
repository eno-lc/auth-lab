package com.jwt.auth.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "6B5970337336763979244226452948404D6351655468576D5A7134743777217A"; // we use the secret key to sign the JWT

    public String extractUserEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject); // we use the extractClaim() method to extract the subject from the JWT, which is the user email
    }

    private Claims extractAllClaims(String jwtToken) { // we use the extractAllClaims() method to extract all the claims from the JWT
        return Jwts // we use the Jwts.parserBuilder() method to create a JWT parser
                .parserBuilder()
                .setSigningKey(getSignInKey()) // we set the signing key
                .build() // we build the JWT parser
                .parseClaimsJws(jwtToken) // we parse the JWT
                .getBody(); // we get the body of the JWT
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken); // we extract all the claims from the JWT
        return claimsResolver.apply(claims); // claimsResolver.apply() method is used to extract a specific claim from the JWT
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){ // we pass the extra claims as a Map object
        return Jwts.builder() // we use the Jwts.builder() method to create a JWT
                .setClaims(extraClaims) // we set the extra claims
                .setSubject(userDetails.getUsername()) // we set the subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // we set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // we set the expiration date
                .signWith(getSignInKey()) // we sign the JWT with the secret key
                .compact(); // we compact the JWT which means we serialize it to a compact URL-safe string
    }

    public String generateToken(UserDetails userDetails){ // we overload the generateToken() method to accept only the UserDetails object
        return generateToken(Map.of(), userDetails); // we pass an empty Map object as the extra claims and the UserDetails object
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // we decode the secret key from base64 to bytes because the Keys.hmacShaKeyFor() method requires a byte array
        return Keys.hmacShaKeyFor(keyBytes); // Keys.hmacShaKeyFor() method returns a Key object that can be used to sign or verify a JWT
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String username = extractUserEmail(jwtToken); // we extract the user email from the JWT
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken)); // we check if the user email extracted from the JWT is the same as the user email from the UserDetails object and if the JWT is not expired
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date()); // we check if the expiration date of the JWT is before the current date
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration); // we use the extractClaim() method to extract the expiration date from the JWT
    }
}
