package com.example.Food.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Allow CORS for all endpoints
                        .allowedOrigins("http://127.0.0.1:5500")  // Your frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allowed HTTP methods
                        .allowedHeaders("*")  // Allow all headers (including Authorization)
                        .allowCredentials(false);  // Since you are using JWT tokens, cookies are not needed
            }
        };
    }
}

