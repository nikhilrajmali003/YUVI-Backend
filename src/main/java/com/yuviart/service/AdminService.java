package com.yuviart.service;

import com.yuviart.dto.AdminLoginRequest;
import com.yuviart.dto.RegisterRequest;
import com.yuviart.model.Admin;
import com.yuviart.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired  // 👈 IMPORTANT: Inject the bean instead of creating new instance
    private BCryptPasswordEncoder passwordEncoder;

    public Admin createAdmin(RegisterRequest request) {
        try {
            System.out.println("📝 Creating admin: " + request.getEmail());

            // Check if admin exists
            Optional<Admin> existing = adminRepository.findByEmail(request.getEmail());
            if (existing.isPresent()) {
                throw new RuntimeException("Email already registered");
            }

            Admin admin = new Admin();
            admin.setName(request.getName());
            admin.setEmail(request.getEmail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setRole("ADMIN");
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            Admin saved = adminRepository.save(admin);
            System.out.println("✅ Admin created with ID: " + saved.getId());

            return saved;

        } catch (Exception e) {
            System.err.println("❌ Error creating admin: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Admin authenticateAdmin(AdminLoginRequest request) {
        try {
            System.out.println("🔐 Authenticating: " + request.getEmail());

            Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());

            if (adminOpt.isEmpty()) {
                System.err.println("❌ Admin not found: " + request.getEmail());
                return null;
            }

            Admin admin = adminOpt.get();
            System.out.println("✅ Admin found: " + admin.getName());
            System.out.println("📧 Email: " + admin.getEmail());
            System.out.println("🆔 ID: " + admin.getId());

            // Check if account is active
            if (admin.getIsActive() != null && !admin.getIsActive()) {
                System.err.println("❌ Account is inactive");
                return null;
            }

            // Check password
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), admin.getPassword());
            System.out.println("🔑 Password match: " + passwordMatches);

            if (passwordMatches) {
                // Update last login
                admin.setLastLogin(LocalDateTime.now());
                adminRepository.save(admin);
                
                System.out.println("✅ Authentication successful!");
                return admin;
            } else {
                System.err.println("❌ Password mismatch");
                System.err.println("⚠️  Provided password length: " + request.getPassword().length());
                return null;
            }

        } catch (Exception e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
