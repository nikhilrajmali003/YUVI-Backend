package com.yuviart.config;

import com.yuviart.model.Admin;
import com.yuviart.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initDefaultAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String defaultEmail = "admin@yuviart.com";
            String defaultPassword = "admin123";
            
            // Check if default admin already exists
            if (!adminRepository.existsByEmail(defaultEmail)) {
                Admin admin = new Admin();
                admin.setEmail(defaultEmail);
                admin.setName("Admin User");
                admin.setPassword(passwordEncoder.encode(defaultPassword));
                admin.setRole("ADMIN");
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                
                adminRepository.save(admin);
                
                System.out.println("═══════════════════════════════════════");
                System.out.println("✅ Default Admin Account Created");
                System.out.println("═══════════════════════════════════════");
                System.out.println("📧 Email: " + defaultEmail);
                System.out.println("🔑 Password: " + defaultPassword);
                System.out.println("═══════════════════════════════════════");
            } else {
                System.out.println("ℹ️ Admin account already exists: " + defaultEmail);
            }
        };
    }
}