package com.bankapp.user_service.security;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JWTService(){
    }

    // USED TO GENERATE TOKEN
    public String generateToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // PUTTING ROLE IN CLAIMS IN TOKEN FOR ROLE BASED ACCESS

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

    // THE BELOW GIVEN METHODS ARE TO EXTRACT USERNAME/USERID AND CLAIMS(includes requests that were sent along with the token) FROM THE TOKEN.

    public String extractUserId(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("JWT token validation failed", e);

            throw new JwtException("Invalid or expired JWT token");
        }
    }

    // THE BELOW GIVEN METHODS ARE TO VALIDATE THE TOKEN

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserId(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

// CONVERTING STRING TYPE SECRET KEY TO SECRETKEY TYPE VIA BYTE[]
//    private SecretKey getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // CONVERTING STRING INTO BYTE[]
//        return Keys.hmacShaKeyFor(keyBytes); // THIS METHOD TAKES INPUT IN BYTE[] FORMAT
//    }


// THIS HELPS IN GENERATING A NEW SECRETKEY EVERY TIME THIS APPLICATION STARTS

//    public JWTService(){
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // .getInstance() method required a try/catch block
//            SecretKey sk = keyGen.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());// Using the above refernce we use generateKey() method to generate a SecretKey type variable
//            System.out.println("==================================================================================================================");
//            System.out.println("JWT Secret Key: " + secretKey);
//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    private String secretKey = "bXlzZWNyZXRrZXltdXN0YmVhdGxlYXN0MzIwbGVuZ3RoIQ==";
