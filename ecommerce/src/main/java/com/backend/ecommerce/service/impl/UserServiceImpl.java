package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.dto.PasswordChangeDto;
import com.backend.ecommerce.dto.UserLoginDto;
import com.backend.ecommerce.dto.UserProfileDto;
import com.backend.ecommerce.dto.UserRegistrationDto;
import com.backend.ecommerce.entity.PasswordResetToken;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.event.UserLoginEvent;
import com.backend.ecommerce.event.UserRegistrationEvent;
import com.backend.ecommerce.repository.PasswordResetTokenRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.JwtService;
import com.backend.ecommerce.service.KafkaProducerService;
import com.backend.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of UserService with comprehensive user management functionality
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private KafkaProducerService kafkaProducerService;
    
    @Value("${password.reset.token.expiration:3600000}")
    private long tokenExpiration;
    
    @Value("${password.reset.base-url}")
    private String resetBaseUrl;
    
    @Override
    @Transactional
    public Map<String, Object> registerUser(UserRegistrationDto userDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if user already exists
            if (userExistsByEmail(userDto.getEmail())) {
                response.put("success", false);
                response.put("error", "Email already registered");
                return response;
            }
            
            // Create new user
            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setRole(User.UserRole.USER);
            user.setActive(true);
            user.setEmailVerified(false);
            user.setPhoneVerified(false);
            
            // Save user
            User savedUser = userRepository.save(user);
            
            // Send Kafka event
            UserRegistrationEvent event = new UserRegistrationEvent(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhoneNumber(),
                savedUser.getRole().toString()
            );
            kafkaProducerService.sendUserRegistrationEvent(event);
            
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            
            logger.info("User registered successfully: {}", savedUser.getEmail());
            
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Registration failed: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> loginUser(UserLoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate credentials
            if (!validateCredentials(loginDto.getEmail(), loginDto.getPassword())) {
                response.put("success", false);
                response.put("error", "Invalid credentials");
                return response;
            }
            
            // Get user
            Optional<User> userOpt = getUserByEmail(loginDto.getEmail());
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Check if user is active
            if (!user.isActive()) {
                response.put("success", false);
                response.put("error", "Account is deactivated");
                return response;
            }
            
            // Update last login
            updateLastLogin(user.getId());
            
            // Generate JWT token
            UserDetails userDetails = loadUserByUsername(user.getEmail());
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("userId", user.getId());
            extraClaims.put("role", user.getRole().toString());
            
            String token = jwtService.generateToken(extraClaims, userDetails);
            
            // Send Kafka event
            UserLoginEvent event = new UserLoginEvent(
                user.getId(),
                user.getEmail(),
                "EMAIL_PASSWORD",
                "Web Client",
                "127.0.0.1",
                true
            );
            kafkaProducerService.sendUserLoginEvent(event);
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", createUserMap(user));
            
            logger.info("User logged in successfully: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Login failed: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> getUserProfile(String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            response.put("success", true);
            response.put("user", createUserMap(user));
            
        } catch (Exception e) {
            logger.error("Error getting user profile: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to get profile: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public Map<String, Object> updateUserProfile(String userId, UserProfileDto profileDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Update fields if provided
            if (profileDto.getFirstName() != null) {
                user.setFirstName(profileDto.getFirstName());
            }
            if (profileDto.getLastName() != null) {
                user.setLastName(profileDto.getLastName());
            }
            if (profileDto.getPhoneNumber() != null) {
                user.setPhoneNumber(profileDto.getPhoneNumber());
            }
            
            // Save updated user
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("user", createUserMap(user));
            
            logger.info("User profile updated: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Error updating user profile: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to update profile: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public Map<String, Object> deleteUser(String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Delete password reset tokens
            passwordResetTokenRepository.deleteAllTokensForUser(userId);
            
            // Delete user
            userRepository.delete(user);
            
            response.put("success", true);
            response.put("message", "Account deleted successfully");
            
            logger.info("User account deleted: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to delete account: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    @Override
    public boolean validateCredentials(String email, String password) {
        Optional<User> userOpt = getUserByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }
    
    @Override
    @Transactional
    public Map<String, Object> requestPasswordReset(String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = getUserByEmail(email);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Generate reset token
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(tokenExpiration / 1000);
            
            // Save token
            PasswordResetToken resetToken = new PasswordResetToken(token, user.getId(), expiryDate);
            passwordResetTokenRepository.save(resetToken);
            
            // TODO: Send email with reset link
            String resetLink = resetBaseUrl + "?token=" + token;
            logger.info("Password reset link generated for {}: {}", user.getEmail(), resetLink);
            
            response.put("success", true);
            response.put("message", "Password reset email sent");
            response.put("resetLink", resetLink); // In production, remove this and send via email
            
        } catch (Exception e) {
            logger.error("Error requesting password reset: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to request password reset: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public Map<String, Object> resetPassword(String token, String newPassword) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);
            if (tokenOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "Invalid reset token");
                return response;
            }
            
            PasswordResetToken resetToken = tokenOpt.get();
            
            // Check if token is valid
            if (!resetToken.isValid()) {
                response.put("success", false);
                response.put("error", "Token expired or already used");
                return response;
            }
            
            // Get user
            Optional<User> userOpt = getUserById(resetToken.getUserId());
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            // Mark token as used
            passwordResetTokenRepository.markTokenAsUsed(resetToken.getId());
            
            response.put("success", true);
            response.put("message", "Password reset successfully");
            
            logger.info("Password reset successfully for user: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to reset password: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public Map<String, Object> changePassword(String userId, PasswordChangeDto passwordChangeDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate password confirmation
            if (!passwordChangeDto.isPasswordMatching()) {
                response.put("success", false);
                response.put("error", "New password and confirmation do not match");
                return response;
            }
            
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Verify current password
            if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())) {
                response.put("success", false);
                response.put("error", "Current password is incorrect");
                return response;
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Password changed successfully");
            
            logger.info("Password changed successfully for user: {}", user.getEmail());
            
        } catch (Exception e) {
            logger.error("Error changing password: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to change password: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> checkEmailExists(String email) {
        Map<String, Object> response = new HashMap<>();
        
        boolean exists = userExistsByEmail(email);
        
        response.put("success", true);
        response.put("available", !exists);
        response.put("message", exists ? "Email already exists" : "Email is available");
        
        return response;
    }
    
    @Override
    public Map<String, Object> getUserStats(String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = getUserById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // TODO: Implement actual statistics from other services
            Map<String, Object> stats = new HashMap<>();
            stats.put("memberSince", user.getCreatedAt());
            stats.put("lastLogin", user.getLastLogin());
            stats.put("emailVerified", user.isEmailVerified());
            stats.put("phoneVerified", user.isPhoneVerified());
            
            response.put("success", true);
            response.put("stats", stats);
            
        } catch (Exception e) {
            logger.error("Error getting user stats: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to get statistics: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Map<String, Object> verifyEmail(String token) {
        // TODO: Implement email verification
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", "Email verification not implemented yet");
        return response;
    }
    
    @Override
    public Map<String, Object> resendEmailVerification(String email) {
        // TODO: Implement email verification resend
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", "Email verification not implemented yet");
        return response;
    }
    
    @Override
    @Transactional
    public void updateLastLogin(String userId) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }
    
    // UserDetailsService implementation
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = getUserByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .disabled(!user.isActive())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .build();
    }
    
    // Helper methods
    private Map<String, Object> createUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("role", user.getRole().toString());
        userMap.put("active", user.isActive());
        userMap.put("emailVerified", user.isEmailVerified());
        userMap.put("phoneVerified", user.isPhoneVerified());
        userMap.put("createdAt", user.getCreatedAt());
        userMap.put("lastLogin", user.getLastLogin());
        
        return userMap;
    }
}
