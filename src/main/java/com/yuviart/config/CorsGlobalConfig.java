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
        registry.addMapping("/**")
                .allowedOriginPatterns(  // ✅ ONE call with all patterns
                    "http://localhost:*",        // All localhost ports
                    "https://*.netlify.app"      // All Netlify URLs
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}