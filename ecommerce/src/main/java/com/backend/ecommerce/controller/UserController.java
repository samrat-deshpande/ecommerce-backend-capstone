package com.backend.ecommerce.controller;

import com.backend.ecommerce.dto.UserRegistrationDto;
import com.backend.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing user operations
 * Provides endpoints for user registration, authentication, and profile management
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs for user registration, authentication, and profile management")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"User registered successfully\", \"userId\": \"uuid-here\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or user already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Email already exists\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationDto userDto) {
        Map<String, Object> response = userService.registerUser(userDto);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * User login
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates user credentials and returns authentication token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Login successful\", \"token\": \"jwt-token-here\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Invalid credentials\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> loginUser(
            @Parameter(description = "User email", example = "user@example.com")
            @RequestParam String email,
            @Parameter(description = "User password", example = "password123")
            @RequestParam String password) {
        
        Map<String, Object> response = userService.loginUser(email, password);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * Get user profile
     */
    @GetMapping("/profile")
    @Operation(
        summary = "Get user profile",
        description = "Retrieves the profile information for the authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"user\": {\"id\": \"uuid\", \"email\": \"user@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<Map<String, Object>> getUserProfile(
            @Parameter(description = "User ID", example = "uuid-here")
            @RequestParam String userId) {
        
        Map<String, Object> response = userService.getUserProfile(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update user profile
     */
    @PutMapping("/profile")
    @Operation(
        summary = "Update user profile",
        description = "Updates the profile information for the authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Profile updated successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Invalid input data\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> updateUserProfile(
            @Parameter(description = "User ID", example = "uuid-here")
            @RequestParam String userId,
            @RequestBody Map<String, Object> profileData) {
        
        Map<String, Object> response = userService.updateUserProfile(userId, profileData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete user account
     */
    @DeleteMapping("/profile")
    @Operation(
        summary = "Delete user account",
        description = "Permanently deletes the user account and all associated data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account deleted successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Account deleted successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<Map<String, Object>> deleteUser(
            @Parameter(description = "User ID", example = "uuid-here")
            @RequestParam String userId) {
        
        Map<String, Object> response = userService.deleteUser(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Request password reset
     */
    @PostMapping("/password/reset-request")
    @Operation(
        summary = "Request password reset",
        description = "Sends a password reset link to the user's email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Password reset email sent",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Password reset email sent\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<Map<String, Object>> requestPasswordReset(
            @Parameter(description = "User email", example = "user@example.com")
            @RequestParam String email) {
        
        Map<String, Object> response = userService.requestPasswordReset(email);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Reset password with token
     */
    @PostMapping("/password/reset")
    @Operation(
        summary = "Reset password with token",
        description = "Resets the user's password using a valid reset token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Password reset successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Password reset successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or expired token",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Invalid or expired token\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Parameter(description = "Reset token", example = "reset-token-here")
            @RequestParam String token,
            @Parameter(description = "New password", example = "newpassword123")
            @RequestParam String newPassword) {
        
        Map<String, Object> response = userService.resetPassword(token, newPassword);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Change password
     */
    @PostMapping("/password/change")
    @Operation(
        summary = "Change password",
        description = "Changes the user's password (requires current password verification)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Password changed successfully\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid current password",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Invalid current password\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> changePassword(
            @Parameter(description = "User ID", example = "uuid-here")
            @RequestParam String userId,
            @Parameter(description = "Current password", example = "currentpassword")
            @RequestParam String currentPassword,
            @Parameter(description = "New password", example = "newpassword123")
            @RequestParam String newPassword) {
        
        Map<String, Object> response = userService.changePassword(userId, currentPassword, newPassword);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Check if email exists
     */
    @GetMapping("/check-email")
    @Operation(
        summary = "Check email availability",
        description = "Checks if an email address is already registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Email availability checked",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"available\": false, \"message\": \"Email already exists\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> checkEmailExists(
            @Parameter(description = "Email to check", example = "user@example.com")
            @RequestParam String email) {
        
        Map<String, Object> response = userService.checkEmailExists(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Get user statistics
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Get user statistics",
        description = "Retrieves statistics about the user's account and activities"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Statistics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"totalOrders\": 5, \"totalSpent\": 299.99, \"memberSince\": \"2024-01-15\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getUserStats(
            @Parameter(description = "User ID", example = "uuid-here")
            @RequestParam String userId) {
        
        Map<String, Object> response = userService.getUserStats(userId);
        return ResponseEntity.ok(response);
    }
}
