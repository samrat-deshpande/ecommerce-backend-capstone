package com.backend.ecommerce.service;

import com.backend.ecommerce.dto.PasswordChangeDto;
import com.backend.ecommerce.dto.UserLoginDto;
import com.backend.ecommerce.dto.UserProfileDto;
import com.backend.ecommerce.dto.UserRegistrationDto;

import java.util.Map;
import java.util.Optional;

/**
 * Service interface for user management operations
 * Defines the contract for user-related business logic
 */
public interface UserService {
    
    /**
     * Register a new user
     * @param userDto User registration data
     * @return Map containing registration result
     */
    Map<String, Object> registerUser(UserRegistrationDto userDto);
    
    /**
     * Authenticate user login
     * @param loginDto User credentials
     * @return Map containing authentication result and token
     */
    Map<String, Object> loginUser(UserLoginDto loginDto);
    
    /**
     * Get user profile by ID
     * @param userId User ID
     * @return Map containing user profile data
     */
    Map<String, Object> getUserProfile(String userId);
    
    /**
     * Update user profile
     * @param userId User ID
     * @param profileDto Updated user profile data
     * @return Map containing update result
     */
    Map<String, Object> updateUserProfile(String userId, UserProfileDto profileDto);
    
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
    
    /**
     * Request password reset
     * @param email User email
     * @return Map containing request result
     */
    Map<String, Object> requestPasswordReset(String email);
    
    /**
     * Reset password with token
     * @param token Reset token
     * @param newPassword New password
     * @return Map containing reset result
     */
    Map<String, Object> resetPassword(String token, String newPassword);
    
    /**
     * Change password (requires current password verification)
     * @param userId User ID
     * @param passwordChangeDto Password change data
     * @return Map containing change result
     */
    Map<String, Object> changePassword(String userId, PasswordChangeDto passwordChangeDto);
    
    /**
     * Check email availability
     * @param email Email to check
     * @return Map containing availability result
     */
    Map<String, Object> checkEmailExists(String email);
    
    /**
     * Get user statistics
     * @param userId User ID
     * @return Map containing user statistics
     */
    Map<String, Object> getUserStats(String userId);
    
    /**
     * Verify email address
     * @param token Verification token
     * @return Map containing verification result
     */
    Map<String, Object> verifyEmail(String token);
    
    /**
     * Resend email verification
     * @param email User email
     * @return Map containing resend result
     */
    Map<String, Object> resendEmailVerification(String email);
    
    /**
     * Update last login time
     * @param userId User ID
     */
    void updateLastLogin(String userId);
    
    /**
     * Get user by email
     * @param email User email
     * @return Optional containing user entity
     */
    Optional<com.backend.ecommerce.entity.User> getUserByEmail(String email);
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return Optional containing user entity
     */
    Optional<com.backend.ecommerce.entity.User> getUserById(String userId);
}
