package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for development and testing purposes
 * This controller provides simple endpoints to verify the application is working
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

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
}
