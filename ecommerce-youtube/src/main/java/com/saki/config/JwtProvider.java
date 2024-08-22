package com.saki.config;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Service
public class JwtProvider {

    // Secret key for signing the JWT
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

   
    public String generateToken(Authentication auth) {
        return Jwts.builder()
            .setIssuedAt(new Date()) // Set the current date as the token issue date
            .setExpiration(new Date(new Date().getTime() + 846000000)) // Set the token expiration date (10 days)
            .claim("email", auth.getName()) // Add the user's email as a claim
            .signWith(key) // Sign the token with the secret key
            .compact();
    }

    /**
     * Extracts the email from the JWT token.
     * 
     * @param jwt the JWT token.
     * @return the email extracted from the token.
     * @throws RuntimeException if the token is invalid or expired.
     */
    public String getEmailFromToken(String jwt) {
        try {
            // Remove the "Bearer " prefix if it exists
            if (jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }

            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Set the secret key for parsing
                .build()
                .parseClaimsJws(jwt)
                .getBody();

            // Return the email claim from the token
            return claims.get("email", String.class);

        } catch (ExpiredJwtException | SignatureException e) {
            // Handle exceptions for invalid or expired tokens
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }
}
