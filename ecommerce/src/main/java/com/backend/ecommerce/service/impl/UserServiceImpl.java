package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService interface
 * Provides business logic for user management operations
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> registerUser(Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate required fields
            if (userData.get("email") == null || userData.get("email").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return response;
            }
            
            if (userData.get("password") == null || userData.get("password").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return response;
            }
            
            if (userData.get("firstName") == null || userData.get("firstName").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "First name is required");
                return response;
            }
            
            if (userData.get("lastName") == null || userData.get("lastName").trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Last name is required");
                return response;
            }
            
            // Check if user already exists
            if (userExistsByEmail(userData.get("email"))) {
                response.put("success", false);
                response.put("message", "User with this email already exists");
                return response;
            }
            
            // Validate password strength
            if (userData.get("password").length() < 6) {
                response.put("success", false);
                response.put("message", "Password must be at least 6 characters long");
                return response;
            }
            
            // Create new user
            User user = new User();
            user.setEmail(userData.get("email").toLowerCase().trim());
            user.setPassword(passwordEncoder.encode(userData.get("password")));
            user.setFirstName(userData.get("firstName").trim());
            user.setLastName(userData.get("lastName").trim());
            user.setPhoneNumber(userData.get("phoneNumber"));
            user.setActive(true);
            user.setEmailVerified(false);
            user.setPhoneVerified(false);
            user.setRole(User.UserRole.USER);
            
            User savedUser = userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to register user: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> loginUser(Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            
            if (email == null || password == null) {
                response.put("success", false);
                response.put("message", "Email and password are required");
                return response;
            }
            
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return response;
            }
            
            User user = userOpt.get();
            
            // Check if user is active
            if (!user.isActive()) {
                response.put("success", false);
                response.put("message", "Account is deactivated. Please contact support.");
                return response;
            }
            
            // Validate password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return response;
            }
            
            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // Generate simple token (in production, use JWT)
            String token = UUID.randomUUID().toString();
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole().toString(),
                "emailVerified", user.isEmailVerified(),
                "phoneVerified", user.isPhoneVerified()
            ));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Optional<Map<String, Object>> getUserProfile(String userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }
            
            User user = userOpt.get();
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", user.getId());
            userProfile.put("email", user.getEmail());
            userProfile.put("firstName", user.getFirstName());
            userProfile.put("lastName", user.getLastName());
            userProfile.put("phoneNumber", user.getPhoneNumber());
            userProfile.put("role", user.getRole().toString());
            userProfile.put("active", user.isActive());
            userProfile.put("emailVerified", user.isEmailVerified());
            userProfile.put("phoneVerified", user.isPhoneVerified());
            userProfile.put("createdAt", user.getCreatedAt());
            userProfile.put("updatedAt", user.getUpdatedAt());
            userProfile.put("lastLogin", user.getLastLogin());
            
            return Optional.of(userProfile);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Object> updateUserProfile(String userId, Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Update allowed fields
            if (userData.get("firstName") != null && !userData.get("firstName").trim().isEmpty()) {
                user.setFirstName(userData.get("firstName").trim());
            }
            
            if (userData.get("lastName") != null && !userData.get("lastName").trim().isEmpty()) {
                user.setLastName(userData.get("lastName").trim());
            }
            
            if (userData.get("phoneNumber") != null) {
                user.setPhoneNumber(userData.get("phoneNumber").trim());
            }
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "User profile updated successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update user profile: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public Map<String, Object> deleteUser(String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Soft delete - mark as inactive
            user.setActive(false);
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "User account deleted successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete user account: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase().trim());
    }

    @Override
    public boolean validateCredentials(String email, String password) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());
            if (userOpt.isEmpty()) {
                return false;
            }
            
            User user = userOpt.get();
            return user.isActive() && passwordEncoder.matches(password, user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Request password reset
     * @param email User's email
     * @return Response with reset status
     */
    public Map<String, Object> requestPasswordReset(String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "If an account with this email exists, a reset link will be sent");
                return response;
            }
            
            User user = userOpt.get();
            if (!user.isActive()) {
                response.put("success", false);
                response.put("message", "Account is deactivated");
                return response;
            }
            
            // Generate reset token (in production, store this securely and set expiration)
            String resetToken = UUID.randomUUID().toString();
            
            // TODO: Send email with reset link
            // For now, just return success message
            
            response.put("success", true);
            response.put("message", "Password reset link has been sent to your email");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to process password reset request: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Reset password using token
     * @param resetToken Password reset token
     * @param newPassword New password
     * @return Response with reset status
     */
    public Map<String, Object> resetPassword(String resetToken, String newPassword) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (newPassword == null || newPassword.length() < 6) {
                response.put("success", false);
                response.put("message", "Password must be at least 6 characters long");
                return response;
            }
            
            // TODO: Validate reset token and find user
            // For now, return success message
            
            response.put("success", true);
            response.put("message", "Password has been reset successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to reset password: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Change password for authenticated user
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     * @return Response with change status
     */
    public Map<String, Object> changePassword(String userId, String currentPassword, String newPassword) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Validate current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                response.put("success", false);
                response.put("message", "Current password is incorrect");
                return response;
            }
            
            // Validate new password
            if (newPassword == null || newPassword.length() < 6) {
                response.put("success", false);
                response.put("message", "New password must be at least 6 characters long");
                return response;
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Password changed successfully");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to change password: " + e.getMessage());
        }
        
        return response;
    }
}
