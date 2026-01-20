package com.yuviart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS Configuration for YuviArt Application
 * Allows React frontend (Netlify) to communicate with Spring Boot backend (Render)
 */
@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply to all endpoints
                .allowedOrigins(
                    "http://localhost:5173",  // Vite local dev
                    "http://localhost:5174",  // Vite alternate
                    "http://localhost:5175",  // Vite alternate
                    "http://localhost:3000",  // Create React App (if used)
                    "https://yuviart.netlify.app",  // ✅ Your Netlify production URL
                    "https://*.netlify.app"  // ✅ Netlify preview/branch URLs
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache preflight requests for 1 hour
    }
}