package com.backend.ecommerce.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * Test Configuration for H2 Database Testing
 * Disables external dependencies like Kafka for isolated testing
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Mock Kafka template for testing
     * This prevents actual Kafka connections during tests
     */
    @Bean
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(null) {
            @Override
            public CompletableFuture<SendResult<String, String>> send(String topic, String data) {
                // Mock implementation - return completed future
                return CompletableFuture.completedFuture(null);
            }
            
            @Override
            public CompletableFuture<SendResult<String, String>> send(String topic, String key, String data) {
                // Mock implementation - return completed future
                return CompletableFuture.completedFuture(null);
            }
        };
    }
}
