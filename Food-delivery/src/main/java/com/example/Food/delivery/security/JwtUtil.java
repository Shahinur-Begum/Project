package com.example.Food.delivery.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  // Secret key for signing the JWT token (this should be in a secure config)

    private final long expiration = 3600000L;  // 1 hour expiration time in milliseconds

    // Generate JWT token with user ID, email, and role
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())  // User ID as the subject
                .claim("email", email)  // Store email as a claim
                .claim("role", "ROLE_" + role.toUpperCase())  // Add "ROLE_" prefix to the role (e.g., ROLE_STUDENT)
                .setIssuedAt(new Date())  // Set the token issue date
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Set expiration date
                .signWith(SignatureAlgorithm.HS256, secret)  // Sign the token with the secret key
                .compact();  // Return the compact JWT token
    }

    // Validate JWT token and return claims
    public Claims validateToken(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(secret)  // Set the signing key
                .parseClaimsJws(token)  // Parse the JWT token
                .getBody();  // Return the claims (e.g., email, role)
    }
}
