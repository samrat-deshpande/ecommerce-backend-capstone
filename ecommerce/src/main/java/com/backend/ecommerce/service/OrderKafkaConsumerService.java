package com.backend.ecommerce.service;

import com.backend.ecommerce.event.OrderEvent;
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

import java.util.concurrent.CompletableFuture;

/**
 * Service for consuming Kafka events related to order and payment activities
 */
@Service
public class OrderKafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaConsumerService.class);
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Consume payment verification responses from Payment Service
     * @param message JSON message from Kafka
     * @param topic Kafka topic name
     */
    @KafkaListener(topics = "${kafka.topic.payment-verification-response:payment-verification-response}")
    public void consumePaymentVerificationResponse(@Payload String message, 
                                                  @Header(KafkaHeaders.TOPIC) String topic) {
        try {
            logger.info("Received payment verification response from topic {}: {}", topic, message);
            
            PaymentEvent paymentEvent = objectMapper.readValue(message, PaymentEvent.class);
            
            // Handle payment verification response
            handlePaymentVerificationResponse(paymentEvent);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize payment verification response: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing payment verification response: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Consume payment status updates from Payment Service
     * @param message JSON message from Kafka
     * @param topic Kafka topic name
     */
    @KafkaListener(topics = "${kafka.topic.payment-status-updates:payment-status-updates}")
    public void consumePaymentStatusUpdate(@Payload String message, 
                                          @Header(KafkaHeaders.TOPIC) String topic) {
        try {
            logger.info("Received payment status update from topic {}: {}", topic, message);
            
            PaymentEvent paymentEvent = objectMapper.readValue(message, PaymentEvent.class);
            
            // Handle payment status update
            handlePaymentStatusUpdate(paymentEvent);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize payment status update: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing payment status update: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Consume user status updates from User Management Service
     * @param message JSON message from Kafka
     * @param topic Kafka topic name
     */
    @KafkaListener(topics = "${kafka.topic.user-status-updates:user-status-updates}")
    public void consumeUserStatusUpdate(@Payload String message, 
                                       @Header(KafkaHeaders.TOPIC) String topic) {
        try {
            logger.info("Received user status update from topic {}: {}", topic, message);
            
            // Handle user status update (e.g., user suspended, account verified, etc.)
            handleUserStatusUpdate(message);
            
        } catch (Exception e) {
            logger.error("Error processing user status update: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Handle payment verification response from Payment Service
     * @param paymentEvent Payment verification response event
     */
    private void handlePaymentVerificationResponse(PaymentEvent paymentEvent) {
        try {
            String orderId = paymentEvent.getOrderId();
            String userId = paymentEvent.getUserId();
            
            if (PaymentEvent.PaymentStatus.SUCCESSFUL.equals(paymentEvent.getStatus())) {
                // Payment verification successful - update order status
                logger.info("Payment verification successful for order {}: {}", orderId, paymentEvent.getTransactionId());
                
                // Update order payment status
                orderService.updateOrderPaymentStatus(orderId, "PAID", userId);
                
                // Update order status to confirmed
                orderService.updateOrderStatus(orderId, "CONFIRMED", userId);
                
            } else if (PaymentEvent.PaymentStatus.FAILED.equals(paymentEvent.getStatus())) {
                // Payment verification failed
                logger.warn("Payment verification failed for order {}: {}", orderId, paymentEvent.getFailureReason());
                
                // Update order payment status
                orderService.updateOrderPaymentStatus(orderId, "FAILED", userId);
                
                // Optionally cancel the order or mark as payment failed
                // orderService.updateOrderStatus(orderId, "PAYMENT_FAILED", userId);
            }
            
        } catch (Exception e) {
            logger.error("Error handling payment verification response: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Handle payment status update from Payment Service
     * @param paymentEvent Payment status update event
     */
    private void handlePaymentStatusUpdate(PaymentEvent paymentEvent) {
        try {
            String orderId = paymentEvent.getOrderId();
            String userId = paymentEvent.getUserId();
            
            switch (paymentEvent.getStatus()) {
                case SUCCESSFUL:
                    logger.info("Payment successful for order {}: {}", orderId, paymentEvent.getTransactionId());
                    orderService.updateOrderPaymentStatus(orderId, "PAID", userId);
                    orderService.updateOrderStatus(orderId, "CONFIRMED", userId);
                    break;
                    
                case FAILED:
                    logger.warn("Payment failed for order {}: {}", orderId, paymentEvent.getFailureReason());
                    orderService.updateOrderPaymentStatus(orderId, "FAILED", userId);
                    break;
                    
                case REFUNDED:
                    logger.info("Payment refunded for order {}: {}", orderId, paymentEvent.getTransactionId());
                    orderService.updateOrderPaymentStatus(orderId, "REFUNDED", userId);
                    orderService.updateOrderStatus(orderId, "REFUNDED", userId);
                    break;
                    
                case PARTIALLY_REFUNDED:
                    logger.info("Payment partially refunded for order {}: {}", orderId, paymentEvent.getTransactionId());
                    orderService.updateOrderPaymentStatus(orderId, "PARTIALLY_REFUNDED", userId);
                    break;
                    
                default:
                    logger.debug("Unhandled payment status: {} for order {}", paymentEvent.getStatus(), orderId);
            }
            
        } catch (Exception e) {
            logger.error("Error handling payment status update: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Handle user status update from User Management Service
     * @param message User status update message
     */
    private void handleUserStatusUpdate(String message) {
        try {
            // Parse user status update message
            // This could include user suspension, account verification, etc.
            logger.info("Processing user status update: {}", message);
            
            // Example: If user is suspended, cancel all pending orders
            // if (userSuspended) {
            //     orderService.cancelUserPendingOrders(userId, "User account suspended");
            // }
            
        } catch (Exception e) {
            logger.error("Error handling user status update: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Process events asynchronously
     * @param task The task to execute
     */
    private void processAsync(Runnable task) {
        CompletableFuture.runAsync(task).exceptionally(throwable -> {
            logger.error("Error in async event processing: {}", throwable.getMessage(), throwable);
            return null;
        });
    }
}
