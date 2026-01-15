package com.yuviart.security;

import com.yuviart.model.User;
import com.yuviart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        // Extract user info from Google
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String providerId = oauth2User.getAttribute("sub"); // Google ID
        String provider = "google";
        
        // Check if user exists
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        
        if (optionalUser.isEmpty()) {
            // Create new user
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setPicture(picture);
            user = userRepository.save(user);
        } else {
            // Update existing user with Google info
            user = optionalUser.get();
            user.setName(name);
            user.setPicture(picture);
            user.setProvider(provider);
            user.setProviderId(providerId);
            user = userRepository.save(user);
        }
        
        return new CustomOAuth2User(oauth2User, user.getId());
    }
}