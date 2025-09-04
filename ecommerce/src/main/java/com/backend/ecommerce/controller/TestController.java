package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.UserRepository;

import java.util.List;
import java.util.ArrayList;

/**
 * Test controller for development and testing purposes
 * This controller provides simple endpoints to verify the application is working
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Simple health check endpoint
     * @return Health status
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Test controller is working!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Simple info endpoint
     * @return Application information
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Ecommerce Backend");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("database", "H2 (Development)");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Root endpoint
     * @return Welcome message
     */
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Ecommerce Backend API!");
        response.put("status", "Running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Test Kafka integration
     * @return Kafka test result
     */
    @GetMapping("/kafka")
    public Map<String, Object> testKafka() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Send a test message to Kafka
            String testMessage = "Test message from Ecommerce Backend at " + System.currentTimeMillis();
            kafkaProducerService.sendMessage("test-topic", "test-key", testMessage);
            
            response.put("status", "SUCCESS");
            response.put("message", "Kafka test message sent successfully!");
            response.put("topic", "test-topic");
            response.put("key", "test-key");
            response.put("message_content", testMessage);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Failed to send Kafka message: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }

    @GetMapping("/generate-password")
    public Map<String, Object> generatePassword() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String plainPassword = "password123";
            String hashedPassword = passwordEncoder.encode(plainPassword);
            
            response.put("success", true);
            response.put("plainPassword", plainPassword);
            response.put("hashedPassword", hashedPassword);
            response.put("message", "Password hash generated successfully");
            
            // Test if the hash matches
            boolean matches = passwordEncoder.matches(plainPassword, hashedPassword);
            response.put("matches", matches);
            
            logger.info("Generated password hash for testing: {}", hashedPassword);
            
        } catch (Exception e) {
            logger.error("Error generating password hash: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to generate password hash: " + e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/test-password")
    public Map<String, Object> testPassword() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String plainPassword = "password123";
            
            // Test the hash that's currently in your database
            String currentHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi";
            
            boolean matches = passwordEncoder.matches(plainPassword, currentHash);
            
            response.put("success", true);
            response.put("plainPassword", plainPassword);
            response.put("currentHash", currentHash);
            response.put("matches", matches);
            response.put("message", matches ? "Password hash matches!" : "Password hash does NOT match!");
            
            // Generate a new hash for comparison
            String newHash = passwordEncoder.encode(plainPassword);
            response.put("newHash", newHash);
            
            logger.info("Password test result: matches={}, currentHash={}, newHash={}", 
                       matches, currentHash, newHash);
            
        } catch (Exception e) {
            logger.error("Error testing password: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to test password: " + e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/test-database")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test if we can find users in the database
            List<User> allUsers = userRepository.findAll();
            List<Map<String, Object>> userList = new ArrayList<>();
            
            for (User user : allUsers) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("email", user.getEmail());
                userInfo.put("firstName", user.getFirstName());
                userInfo.put("lastName", user.getLastName());
                userInfo.put("password", user.getPassword());
                userInfo.put("role", user.getRole());
                userInfo.put("active", user.isActive());
                userInfo.put("emailVerified", user.isEmailVerified());
                userInfo.put("phoneVerified", user.isPhoneVerified());
                userList.add(userInfo);
            }
            
            response.put("success", true);
            response.put("totalUsers", allUsers.size());
            response.put("users", userList);
            response.put("message", "Database connection successful");
            
            logger.info("Database test successful. Found {} users", allUsers.size());
            
        } catch (Exception e) {
            logger.error("Error testing database: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", "Failed to test database: " + e.getMessage());
        }
        
        return response;
    }
}
