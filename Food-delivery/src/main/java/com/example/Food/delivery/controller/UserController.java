package com.example.Food.delivery.controller;

import com.example.Food.delivery.dto.GoogleLoginDto;
import com.example.Food.delivery.dto.UserLoginDto;
import com.example.Food.delivery.dto.UserSignupDto;
import com.example.Food.delivery.Entity.User;
import com.example.Food.delivery.security.JwtUtil;
import com.example.Food.delivery.service.GoogleTokenVerifierService;
import com.example.Food.delivery.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleTokenVerifierService googleVerifier;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupDto dto) {
        try {
            // 1. Signup and create a new user
            User user = userService.signup(dto);

            // 2. Generate JWT token for the new user
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

            // 3. Return token in response
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // Handle errors and return error message
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        try {
            // 1. Login the user using the provided credentials
            User user = userService.login(dto);

            // 2. Generate JWT token for the authenticated user
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

            // 3. Return token in response
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // Handle invalid login credentials and return error message
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginDto dto) {
        try {
            // 1. Verify Google ID token and get user info
            Payload payload = googleVerifier.verifyToken(dto.getIdToken());
            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // 2. Check if the user exists or create a new user
            User user = userService.googleLogin(googleId, email, name);

            // 3. Generate JWT token for the user
            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

            // 4. Return token in response
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // Handle invalid Google token and return error message
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // A protected endpoint that requires role-based authorization
    @GetMapping("/test-auth")
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_VENDOR')")
    public ResponseEntity<?> testAuth(Authentication authentication) {
        String email = authentication.getName();  // Extract email from JWT token
        return ResponseEntity.ok(Map.of(
                "message", "You are authenticated!",
                "email", email
        ));
    }
}
