package com.yuviart.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException {
        
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        
        // Create simple token (in production, use JWT)
        String userJson = String.format(
            "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"picture\":\"%s\"}",
            oauth2User.getUserId(),
            oauth2User.getName(),
            oauth2User.getEmail(),
            oauth2User.getPicture()
        );
        String token = Base64.getEncoder().encodeToString(userJson.getBytes());
        
        // Redirect to frontend with token and user info
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/success")
                .queryParam("token", token)
                .queryParam("name", URLEncoder.encode(oauth2User.getName(), StandardCharsets.UTF_8))
                .queryParam("email", URLEncoder.encode(oauth2User.getEmail(), StandardCharsets.UTF_8))
                .build()
                .toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}