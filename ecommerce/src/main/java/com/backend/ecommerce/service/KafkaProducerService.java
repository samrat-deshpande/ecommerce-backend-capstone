package com.backend.ecommerce.service;

import com.backend.ecommerce.event.UserEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for producing Kafka events related to user activities
 */
@Service
public class KafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${kafka.topic.user-events:user-events}")
    private String userEventsTopic;
    
    @Value("${kafka.topic.user-registrations:user-registrations}")
    private String userRegistrationsTopic;
    
    @Value("${kafka.topic.user-logins:user-logins}")
    private String userLoginsTopic;
    
    /**
     * Send a user event to the general user events topic
     * @param event The user event to send
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendUserEvent(UserEvent event) {
        return sendEvent(userEventsTopic, event);
    }
    
    /**
     * Send a user registration event to the dedicated topic
     * @param event The user registration event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendUserRegistrationEvent(UserEvent event) {
        return sendEvent(userRegistrationsTopic, event);
    }
    
    /**
     * Send a user login event to the dedicated topic
     * @param event The user login event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendUserLoginEvent(UserEvent event) {
        return sendEvent(userLoginsTopic, event);
    }
    
    /**
     * Generic method to send an event to a specific topic
     * @param topic The Kafka topic to send to
     * @param event The event to send
     * @return CompletableFuture for the send operation
     */
    private CompletableFuture<SendResult<String, String>> sendEvent(String topic, UserEvent event) {
        try {
            // Generate event ID if not present
            if (event.getEventId() == null) {
                event.setEventId(UUID.randomUUID().toString());
            }
            
            String eventJson = objectMapper.writeValueAsString(event);
            String key = event.getUserId() != null ? event.getUserId() : event.getUserEmail();
            
            logger.info("Sending event to topic {}: {}", topic, eventJson);
            
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, eventJson);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Event sent successfully to topic {}: {}", topic, result.getRecordMetadata());
                } else {
                    logger.error("Failed to send event to topic {}: {}", topic, ex.getMessage(), ex);
                }
            });
            
            return future;
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event: {}", e.getMessage(), e);
            CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
    
    /**
     * Send a simple message to a topic
     * @param topic The Kafka topic
     * @param key The message key
     * @param message The message content
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendMessage(String topic, String key, String message) {
        logger.info("Sending message to topic {} with key {}: {}", topic, key, message);
        
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Message sent successfully to topic {}: {}", topic, result.getRecordMetadata());
            } else {
                logger.error("Failed to send message to topic {}: {}", topic, ex.getMessage(), ex);
            }
        });
        
        return future;
    }
}
