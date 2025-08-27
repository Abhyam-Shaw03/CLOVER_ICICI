package com.bankapp.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.expiration:1800000}") // fallback to 30 minutes
    private long jwtExpirationInMs;

    private SecretKey key;

    private static final Logger log = LoggerFactory.getLogger(JWTService.class);

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT secret key. Please provide a valid Base64 encoded key.", e);
            throw new IllegalStateException("JWT secret key is not properly configured.", e);
        }
    }

    public JWTService(){
    }

    // This method is used to GENERATE a JWT token with a user ID and a role.
    public String generateToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Puts the role in the claims for role-based access

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .and()
                .signWith(key)
                .compact();
    }

    // This method EXTRACTS the user ID from the JWT token.
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // This method EXTRACTS the user role from the JWT token.
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // This is the main method for VALIDATING a token.
    // It verifies the token's signature and expiration, and checks if the username matches.
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String userName = extractUserId(token);
            // The Jwts.parser() call in extractUserId already performs signature and expiration checks implicitly.
            // This line only needs to check if the username matches the UserDetails.
            return (userName.equals(userDetails.getUsername()));
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}