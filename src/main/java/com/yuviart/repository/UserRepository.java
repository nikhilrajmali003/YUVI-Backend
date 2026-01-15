package com.yuviart.repository;

import com.yuviart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     * Used for: Login, Registration check, OAuth user lookup
     * 
     * @param email - User's email address
     * @return Optional<User> - User if found, empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by OAuth provider and provider ID
     * Used for: OAuth login (Google, Facebook, etc.)
     * 
     * @param provider - OAuth provider name (e.g., "google", "facebook")
     * @param providerId - Unique ID from the OAuth provider
     * @return Optional<User> - User if found, empty if not found
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    
    /**
     * Check if a user exists with the given email
     * Used for: Registration validation
     * 
     * @param email - User's email address
     * @return boolean - true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find user by ID
     * (Already provided by JpaRepository, just documenting for clarity)
     * 
     * @param id - User's ID
     * @return Optional<User>
     */
    // Optional<User> findById(Long id); // Already inherited from JpaRepository
    
    /**
     * Save or update user
     * (Already provided by JpaRepository)
     * 
     * @param user - User entity to save
     * @return User - Saved user entity
     */
    // User save(User user); // Already inherited from JpaRepository
    
    /**
     * Delete user by ID
     * (Already provided by JpaRepository)
     * 
     * @param id - User's ID
     */
    // void deleteById(Long id); // Already inherited from JpaRepository
}