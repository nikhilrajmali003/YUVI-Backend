package com.yuviart.repository;

import com.yuviart.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    /**
     * Find admin by email address
     * @param email Admin email
     * @return Optional Admin object
     */
    Optional<Admin> findByEmail(String email);
    
    /**
     * Check if email already exists in database
     * @param email Admin email
     * @return true if email exists, false otherwise
     */
    Boolean existsByEmail(String email);
}