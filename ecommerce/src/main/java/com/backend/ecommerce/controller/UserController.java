package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for managing user operations
 * Provides endpoints for user registration, authentication, profile management, and password operations
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * @param userData User registration data
     * @return Response with user creation status
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = userService.registerUser(userData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Authenticate user login
     * @param loginData User credentials
     * @return Response with authentication token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = userService.loginUser(loginData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * Get user profile information
     * @param userId User ID
     * @return User profile data
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String userId) {
        var userProfileOpt = userService.getUserProfile(userId);
        
        if (userProfileOpt.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", userProfileOpt.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update user profile
     * @param userId User ID
     * @param userData Updated user data
     * @return Response with update status
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @PathVariable String userId,
            @RequestBody Map<String, String> userData) {
        Map<String, Object> response = userService.updateUserProfile(userId, userData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete user account
     * @param userId User ID
     * @return Response with deletion status
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String userId) {
        Map<String, Object> response = userService.deleteUser(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Request password reset
     * @param requestData Email for password reset
     * @return Response with reset request status
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Cast to UserServiceImpl to access the additional methods
        if (userService instanceof com.backend.ecommerce.service.impl.UserServiceImpl) {
            com.backend.ecommerce.service.impl.UserServiceImpl userServiceImpl = 
                (com.backend.ecommerce.service.impl.UserServiceImpl) userService;
            
            Map<String, Object> response = userServiceImpl.requestPasswordReset(email);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Password reset service not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Reset password using token
     * @param resetData Reset token and new password
     * @return Response with reset status
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> resetData) {
        String resetToken = resetData.get("resetToken");
        String newPassword = resetData.get("newPassword");
        
        if (resetToken == null || newPassword == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Reset token and new password are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Cast to UserServiceImpl to access the additional methods
        if (userService instanceof com.backend.ecommerce.service.impl.UserServiceImpl) {
            com.backend.ecommerce.service.impl.UserServiceImpl userServiceImpl = 
                (com.backend.ecommerce.service.impl.UserServiceImpl) userService;
            
            Map<String, Object> response = userServiceImpl.resetPassword(resetToken, newPassword);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Password reset service not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Change password for authenticated user
     * @param userId User ID
     * @param passwordData Current and new password
     * @return Response with change status
     */
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable String userId,
            @RequestBody Map<String, String> passwordData) {
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");
        
        if (currentPassword == null || newPassword == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Current password and new password are required");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Cast to UserServiceImpl to access the additional methods
        if (userService instanceof com.backend.ecommerce.service.impl.UserServiceImpl) {
            com.backend.ecommerce.service.impl.UserServiceImpl userServiceImpl = 
                (com.backend.ecommerce.service.impl.UserServiceImpl) userService;
            
            Map<String, Object> response = userServiceImpl.changePassword(userId, currentPassword, newPassword);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Password change service not available");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Verify if email exists
     * @param email Email to check
     * @return Response indicating if email exists
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.userExistsByEmail(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("emailExists", exists);
        response.put("message", exists ? "Email already exists" : "Email is available");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get user statistics (for admin use)
     * @return User statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        // TODO: Implement user statistics
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("stats", Map.of(
            "totalUsers", 0,
            "activeUsers", 0,
            "newUsersToday", 0,
            "verifiedUsers", 0
        ));
        
        return ResponseEntity.ok(response);
    }
}
