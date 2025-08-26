package com.backend.ecommerce.service;

import com.backend.ecommerce.event.OrderEvent;
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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for producing Kafka events related to order and payment activities
 */
@Service
public class OrderKafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${kafka.topic.order-events:order-events}")
    private String orderEventsTopic;
    
    @Value("${kafka.topic.payment-events:payment-events}")
    private String paymentEventsTopic;
    
    @Value("${kafka.topic.order-status-updates:order-status-updates}")
    private String orderStatusUpdatesTopic;
    
    @Value("${kafka.topic.payment-verification:payment-verification}")
    private String paymentVerificationTopic;
    
    /**
     * Send an order event to the general order events topic
     * @param event The order event to send
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderEvent(OrderEvent event) {
        return sendEvent(orderEventsTopic, event);
    }
    
    /**
     * Send an order status update event
     * @param event The order status update event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderStatusUpdate(OrderEvent event) {
        return sendEvent(orderStatusUpdatesTopic, event);
    }
    
    /**
     * Send a payment event to the payment events topic
     * @param event The payment event to send
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentEvent(PaymentEvent event) {
        return sendEvent(paymentEventsTopic, event);
    }
    
    /**
     * Send a payment verification request
     * @param event The payment verification event
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentVerificationRequest(PaymentEvent event) {
        return sendEvent(paymentVerificationTopic, event);
    }
    
    /**
     * Send order created event
     * @param orderId Order ID
     * @param userId User ID
     * @param orderNumber Order number
     * @param totalAmount Total amount
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderCreatedEvent(String orderId, String userId, String orderNumber, java.math.BigDecimal totalAmount) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_CREATED, orderId, userId);
        event.setOrderNumber(orderNumber);
        event.setTotalAmount(totalAmount);
        event.setStatus(OrderEvent.OrderStatus.PENDING);
        event.setPaymentStatus(OrderEvent.PaymentStatus.PENDING);
        return sendOrderEvent(event);
    }
    
    /**
     * Send order confirmed event
     * @param orderId Order ID
     * @param userId User ID
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderConfirmedEvent(String orderId, String userId) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_CONFIRMED, orderId, userId);
        event.setStatus(OrderEvent.OrderStatus.CONFIRMED);
        return sendOrderStatusUpdate(event);
    }
    
    /**
     * Send order paid event
     * @param orderId Order ID
     * @param userId User ID
     * @param paymentTransactionId Payment transaction ID
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderPaidEvent(String orderId, String userId, String paymentTransactionId) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_PAID, orderId, userId);
        event.setStatus(OrderEvent.OrderStatus.PAID);
        event.setPaymentStatus(OrderEvent.PaymentStatus.PAID);
        event.setPaymentTransactionId(paymentTransactionId);
        return sendOrderStatusUpdate(event);
    }
    
    /**
     * Send order shipped event
     * @param orderId Order ID
     * @param userId User ID
     * @param trackingNumber Tracking number
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderShippedEvent(String orderId, String userId, String trackingNumber) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_SHIPPED, orderId, userId);
        event.setStatus(OrderEvent.OrderStatus.SHIPPED);
        event.setTrackingNumber(trackingNumber);
        return sendOrderStatusUpdate(event);
    }
    
    /**
     * Send order delivered event
     * @param orderId Order ID
     * @param userId User ID
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderDeliveredEvent(String orderId, String userId) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_DELIVERED, orderId, userId);
        event.setStatus(OrderEvent.OrderStatus.DELIVERED);
        return sendOrderStatusUpdate(event);
    }
    
    /**
     * Send order cancelled event
     * @param orderId Order ID
     * @param userId User ID
     * @param reason Cancellation reason
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendOrderCancelledEvent(String orderId, String userId, String reason) {
        OrderEvent event = new OrderEvent(OrderEvent.ORDER_CANCELLED, orderId, userId);
        event.setStatus(OrderEvent.OrderStatus.CANCELLED);
        event.setMetadata(java.util.Map.of("cancellationReason", reason));
        return sendOrderStatusUpdate(event);
    }
    
    /**
     * Send payment verification request
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return CompletableFuture for the send operation
     */
    public CompletableFuture<SendResult<String, String>> sendPaymentVerificationRequest(String orderId, String userId, java.math.BigDecimal amount, String paymentMethod) {
        PaymentEvent event = new PaymentEvent(PaymentEvent.PAYMENT_VERIFICATION_REQUESTED, orderId, userId, amount);
        event.setPaymentMethod(paymentMethod);
        event.setStatus(PaymentEvent.PaymentStatus.PENDING);
        return sendPaymentVerificationRequest(event);
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
     * Extract key from event for Kafka partitioning
     * @param event The event to extract key from
     * @return String key for Kafka
     */
    private <T> String extractKey(T event) {
        if (event instanceof OrderEvent) {
            OrderEvent orderEvent = (OrderEvent) event;
            return orderEvent.getOrderId() != null ? orderEvent.getOrderId() : orderEvent.getUserId();
        } else if (event instanceof PaymentEvent) {
            PaymentEvent paymentEvent = (PaymentEvent) event;
            return paymentEvent.getOrderId() != null ? paymentEvent.getOrderId() : paymentEvent.getUserId();
        }
        return UUID.randomUUID().toString();
    }
}
