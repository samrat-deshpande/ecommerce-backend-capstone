package com.backend.ecommerce.service;

import com.backend.ecommerce.event.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * Service for consuming Kafka events related to payment activities
 */
@Service
public class PaymentKafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentKafkaConsumerService.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PaymentKafkaProducerService kafkaProducerService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Consume payment verification requests from Order Management Service
     * @param message JSON message from Kafka
     * @param topic Kafka topic name
     */
    @KafkaListener(topics = "${kafka.topic.payment-verification:payment-verification}")
    public void consumePaymentVerificationRequest(@Payload String message, 
                                                 @Header(KafkaHeaders.TOPIC) String topic) {
        try {
            logger.info("Received payment verification request from topic {}: {}", topic, message);
            
            PaymentEvent paymentEvent = objectMapper.readValue(message, PaymentEvent.class);
            
            // Handle payment verification request
            handlePaymentVerificationRequest(paymentEvent);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize payment verification request: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing payment verification request: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Handle payment verification request from Order Management Service
     * @param paymentEvent Payment verification request event
     */
    private void handlePaymentVerificationRequest(PaymentEvent paymentEvent) {
        try {
            String orderId = paymentEvent.getOrderId();
            String userId = paymentEvent.getUserId();
            BigDecimal amount = paymentEvent.getAmount();
            String paymentMethod = paymentEvent.getPaymentMethod();
            
            logger.info("Processing payment verification request for order {}: amount={}, method={}", 
                       orderId, amount, paymentMethod);
            
            // Send payment initiated event
            kafkaProducerService.sendPaymentInitiatedEvent(orderId, userId, amount, paymentMethod);
            
            // Send payment processing event
            kafkaProducerService.sendPaymentProcessingEvent(orderId, userId, amount, paymentMethod);
            
            // Process the payment through the gateway
            processPaymentForOrder(orderId, userId, amount, paymentMethod);
            
        } catch (Exception e) {
            logger.error("Error handling payment verification request: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Process payment for an order
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     */
    private void processPaymentForOrder(String orderId, String userId, BigDecimal amount, String paymentMethod) {
        try {
            // Create mock payment data (in real implementation, this would come from the request)
            java.util.Map<String, Object> paymentData = new java.util.HashMap<>();
            paymentData.put("cardNumber", "****-****-****-1234");
            paymentData.put("expiryMonth", "12");
            paymentData.put("expiryYear", "2026");
            paymentData.put("cvv", "123");
            paymentData.put("cardholderName", "John Doe");
            
            // Process payment through the service
            java.util.Map<String, Object> paymentResult = paymentService.processPayment(
                orderId, userId, amount, paymentMethod, paymentData
            );
            
            // Handle payment result
            if ((Boolean) paymentResult.get("success")) {
                String transactionId = (String) paymentResult.get("transactionId");
                
                // Send payment successful event
                kafkaProducerService.sendPaymentSuccessfulEvent(orderId, userId, amount, paymentMethod, transactionId);
                
                // Send payment verification response
                kafkaProducerService.sendPaymentVerificationResponse(orderId, userId, amount, transactionId, true);
                
                logger.info("Payment processed successfully for order {}: transactionId={}", orderId, transactionId);
                
            } else {
                String failureReason = (String) paymentResult.get("failureReason");
                
                // Send payment failed event
                kafkaProducerService.sendPaymentFailedEvent(orderId, userId, amount, paymentMethod, failureReason);
                
                // Send payment verification response (failed)
                kafkaProducerService.sendPaymentVerificationResponse(orderId, userId, amount, null, false);
                
                logger.warn("Payment failed for order {}: reason={}", orderId, failureReason);
            }
            
        } catch (Exception e) {
            logger.error("Error processing payment for order {}: {}", orderId, e.getMessage(), e);
            
            // Send payment failed event due to processing error
            kafkaProducerService.sendPaymentFailedEvent(orderId, userId, amount, paymentMethod, "Processing error: " + e.getMessage());
            
            // Send payment verification response (failed)
            kafkaProducerService.sendPaymentVerificationResponse(orderId, userId, amount, null, false);
        }
    }
    
    /**
     * Process events asynchronously
     * @param task The task to execute
     */
    private void processAsync(Runnable task) {
        CompletableFuture.runAsync(task).exceptionally(throwable -> {
            logger.error("Error in async payment event processing: {}", throwable.getMessage(), throwable);
            return null;
        });
    }
}
