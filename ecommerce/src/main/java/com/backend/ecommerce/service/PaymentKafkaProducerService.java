package com.backend.ecommerce.service;

import com.backend.ecommerce.event.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for producing Kafka events related to payment activities
 */
@Service
public class PaymentKafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentKafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${kafka.topic.payment-events:payment-events}")
    private String paymentEventsTopic;
    
    @Value("${kafka.topic.payment-status-updates:payment-status-updates}")
    private String paymentStatusUpdatesTopic;
    
    @Value("${kafka.topic.payment-verification-response:payment-verification-response}")
    private String paymentVerificationResponseTopic;
    
    @Value("${kafka.topic.payment-refunds:payment-refunds}")
    private String paymentRefundsTopic;
    
    /**
     * Send a payment event to the general payment events topic
     * @param event The payment event to send
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentEvent(PaymentEvent event) {
        return sendEvent(paymentEventsTopic, event);
    }
    
    /**
     * Send a payment status update event
     * @param event The payment status update event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentStatusUpdate(PaymentEvent event) {
        return sendEvent(paymentStatusUpdatesTopic, event);
    }
    
    /**
     * Send a payment verification response
     * @param event The payment verification response event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentVerificationResponse(PaymentEvent event) {
        return sendEvent(paymentVerificationResponseTopic, event);
    }
    
    /**
     * Send a payment refund event
     * @param event The payment refund event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentRefundEvent(PaymentEvent event) {
        return sendEvent(paymentRefundsTopic, event);
    }
    
    /**
     * Send payment initiated event
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentInitiatedEvent(String orderId, String userId, BigDecimal amount, String paymentMethod) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_INITIATED, orderId, userId, amount);
        event.setPaymentMethod(paymentMethod);
        event.setStatus(PaymentEvent.PaymentStatus.PROCESSING);
        return sendPaymentEvent(event);
    }
    
    /**
     * Send payment processing event
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentProcessingEvent(String orderId, String userId, BigDecimal amount, String paymentMethod) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_PROCESSING, orderId, userId, amount);
        event.setPaymentMethod(paymentMethod);
        event.setStatus(PaymentEvent.PaymentStatus.PROCESSING);
        return sendPaymentEvent(event);
    }
    
    /**
     * Send payment successful event
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @param transactionId Transaction ID from gateway
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentSuccessfulEvent(String orderId, String userId, BigDecimal amount, String paymentMethod, String transactionId) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_SUCCESSFUL, orderId, userId, amount);
        event.setPaymentMethod(paymentMethod);
        event.setTransactionId(transactionId);
        event.setStatus(PaymentEvent.PaymentStatus.SUCCESSFUL);
        event.setGatewayResponse("Payment processed successfully");
        return sendPaymentStatusUpdate(event);
    }
    
    /**
     * Send payment failed event
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @param failureReason Reason for failure
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentFailedEvent(String orderId, String userId, BigDecimal amount, String paymentMethod, String failureReason) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_FAILED, orderId, userId, amount);
        event.setPaymentMethod(paymentMethod);
        event.setStatus(PaymentEvent.PaymentStatus.FAILED);
        event.setFailureReason(failureReason);
        event.setGatewayResponse("Payment failed");
        return sendPaymentStatusUpdate(event);
    }
    
    /**
     * Send payment verification response
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param transactionId Transaction ID
     * @param isVerified Whether payment is verified
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentVerificationResponse(String orderId, String userId, BigDecimal amount, String transactionId, boolean isVerified) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_VERIFICATION_COMPLETED, orderId, userId, amount);
        event.setTransactionId(transactionId);
        event.setStatus(isVerified ? PaymentEvent.PaymentStatus.SUCCESSFUL : PaymentEvent.PaymentStatus.FAILED);
        event.setGatewayResponse(isVerified ? "Payment verified successfully" : "Payment verification failed");
        return sendPaymentVerificationResponse(event);
    }
    
    /**
     * Send payment refunded event
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Refund amount
     * @param transactionId Original transaction ID
     * @param refundId Refund ID
     * @param reason Refund reason
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentRefundedEvent(String orderId, String userId, BigDecimal amount, String transactionId, String refundId, String reason) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_REFUNDED, orderId, userId, amount);
        event.setTransactionId(transactionId);
        event.setPaymentId(refundId);
        event.setStatus(PaymentEvent.PaymentStatus.REFUNDED);
        event.setMetadata(java.util.Map.of("refundReason", reason));
        event.setGatewayResponse("Refund processed successfully");
        return sendPaymentRefundEvent(event);
    }
    
    /**
     * Generic method to send an event to a specific topic
     * @param topic The Kafka topic to send to
     * @param event The event to send
     * @return CompletableFuture for the send operation
     */
    private <T> CompletableFuture<SendResult<String, String>> sendEvent(String topic, T event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            String key = extractKey(event);
            
            logger.info("Sending payment event to topic {}: {}", topic, eventJson);
            
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, eventJson);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Payment event sent successfully to topic {}: {}", topic, result.getRecordMetadata());
                } else {
                    logger.error("Failed to send payment event to topic {}: {}", topic, ex.getMessage(), ex);
                }
            });
            
            return future;
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize payment event: {}", e.getMessage(), e);
            CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
    
    /**
     * Extract key from event for Kafka partitioning
     * @param event The event to extract key from
     * @return String key for Kafka
     */
    private <T> String extractKey(T event) {
        if (event instanceof PaymentEvent) {
            PaymentEvent paymentEvent = (PaymentEvent) event;
            return paymentEvent.getOrderId() != null ? paymentEvent.getOrderId() : paymentEvent.getUserId();
        }
        return UUID.randomUUID().toString();
    }
}
