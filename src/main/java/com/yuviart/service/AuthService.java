package com.yuviart.service;

import com.yuviart.dto.LoginRequest;
import com.yuviart.dto.LoginResponse;
import com.yuviart.dto.RegisterRequest;
import com.yuviart.model.User;
import com.yuviart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.yuviart.config.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Register new user
     */
    public LoginResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }
        
        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProvider("local"); // Mark as local registration
        
        // Save user
        user = userRepository.save(user);
        
        // Return response
        return new LoginResponse(
            user.getId(), 
            user.getName(), 
            user.getEmail(),
            jwtTokenProvider.generateToken(user.getEmail()) // ✅ Real JWT token
        );
    }
    
    /**
     * Login user
     */
    public LoginResponse login(LoginRequest request) {
        // Find user by email
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        User user = optionalUser.get();
        
        // Check if user registered with OAuth
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("This account was created with Google. Please sign in with Google.");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        // Return response
        return new LoginResponse(
            user.getId(), 
            user.getName(), 
            user.getEmail(),
            jwtTokenProvider.generateToken(user.getEmail()) // ✅ Real JWT token
        );
    }
}