package com.yuviart.controller;

import com.yuviart.dto.AdminLoginRequest;
import com.yuviart.model.Admin;
import com.yuviart.service.AdminService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController

@RequestMapping("/api/admin")

@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private com.yuviart.config.JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequest request) {
        try {
            System.out.println("🔐 Login attempt for: " + request.getEmail());

            Admin admin = adminService.authenticateAdmin(request);

            if (admin != null) {
                System.out.println("✅ Login successful!");

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwtTokenProvider.generateToken(admin.getEmail()));
                response.put("admin", admin);
                response.put("message", "Login successful");

                return ResponseEntity.ok(response);
            } else {
                System.err.println("❌ Invalid credentials");

                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}