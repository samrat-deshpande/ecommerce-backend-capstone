package com.backend.ecommerce.service;

import java.util.Map;
import java.util.Optional;

/**
 * Service interface for user management operations
 * Defines the contract for user-related business logic
 */
public interface UserService {
    
    /**
     * Register a new user
     * @param userData User registration data
     * @return Map containing registration result
     */
    Map<String, Object> registerUser(Map<String, String> userData);
    
    /**
     * Authenticate user login
     * @param loginData User credentials
     * @return Map containing authentication result and token
     */
    Map<String, Object> loginUser(Map<String, String> loginData);
    
    /**
     * Get user profile by ID
     * @param userId User ID
     * @return Optional containing user profile data
     */
    Optional<Map<String, Object>> getUserProfile(String userId);
    
    /**
     * Update user profile
     * @param userId User ID
     * @param userData Updated user data
     * @return Map containing update result
     */
    Map<String, Object> updateUserProfile(String userId, Map<String, String> userData);
    
    /**
     * Delete user account
     * @param userId User ID
     * @return Map containing deletion result
     */
    Map<String, Object> deleteUser(String userId);
    
    /**
     * Check if user exists by email
     * @param email User email
     * @return true if user exists, false otherwise
     */
    boolean userExistsByEmail(String email);
    
    /**
     * Validate user credentials
     * @param email User email
     * @param password User password
     * @return true if credentials are valid, false otherwise
     */
    boolean validateCredentials(String email, String password);
}
