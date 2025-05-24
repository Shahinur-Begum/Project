package com.example.Food.delivery.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // Extract token from the header

            try {
                Claims claims = jwtUtil.validateToken(token);  // Validate the token

                String email = claims.get("email", String.class);  // Extract email from the claims
                String role = claims.get("role", String.class);  // Extract role from the claims

                // If the email is not null and there is no existing authentication, create a new authentication object
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);  // Set role authority

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, Collections.singleton(authority));

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // Set details like IP address
                    SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication context
                }

            } catch (Exception e) {
                System.out.println("Invalid Token: " + e.getMessage());  // Optionally log token validation errors
            }
        }

        filterChain.doFilter(request, response);  // Continue the filter chain
    }
}
