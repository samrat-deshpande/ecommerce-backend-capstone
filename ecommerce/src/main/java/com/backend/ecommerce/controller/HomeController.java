package com.backend.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Home controller for the main application
 * Provides basic information and welcome message
 */
@RestController
public class HomeController {

    /**
     * Root endpoint
     * @return Welcome message and application information
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Ecommerce Backend API!");
        response.put("version", "1.0.0");
        response.put("status", "Running");
        response.put("database", "H2 (Development)");
        response.put("timestamp", System.currentTimeMillis());
        response.put("endpoints", new HashMap<String, String>() {{
            put("catalog", "/api/catalog/**");
            put("users", "/api/users/**");
            put("test", "/api/test/**");
            put("h2-console", "/h2-console");
            put("actuator", "/actuator/**");
            put("swagger", "/swagger-ui/index.html");
        }});
        return response;
    }

    /**
     * Health check endpoint
     * @return Health status
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is healthy!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
