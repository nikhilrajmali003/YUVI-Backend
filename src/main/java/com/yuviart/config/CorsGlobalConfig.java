package com.yuviart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS Configuration for YuviArt Application
 * Allows React frontend to communicate with Spring Boot backend
 */
@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply to all endpoints
                .allowedOrigins(
                    "http://localhost:5173",  // Vite default port
                    "http://localhost:5174",  // Vite alternate port
                    "http://localhost:5175",  // Vite alternate port
                    "http://localhost:3000",  // Create React App port (optional)
                    "https://artgallery-hazel.vercel.app/"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache preflight requests for 1 hour
    }
}