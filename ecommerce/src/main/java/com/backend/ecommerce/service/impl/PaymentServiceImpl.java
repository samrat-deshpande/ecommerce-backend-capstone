package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.Payment;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.PaymentRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.PaymentService;
import com.backend.ecommerce.service.PaymentGatewayService;
import com.backend.ecommerce.service.PaymentKafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of Payment Service with gateway integration and Kafka event publishing
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    @Qualifier("stripePaymentGateway")
    private PaymentGatewayService paymentGateway;

    @Autowired
    private PaymentKafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public Map<String, Object> processPayment(String orderId, String userId, BigDecimal amount, String paymentMethod, Map<String, Object> paymentData) {
        try {
            logger.info("Processing payment for order {}: amount={}, method={}", orderId, amount, paymentMethod);
            
            // Validate user
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Create payment record
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
            payment.setStatus(Payment.PaymentStatus.PROCESSING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

            // Save payment record
            Payment savedPayment = paymentRepository.save(payment);
            
            // Process payment through gateway
            Map<String, Object> gatewayResponse = paymentGateway.processPayment(amount, paymentMethod, paymentData);
            
            // Update payment record with gateway response
            if ((Boolean) gatewayResponse.get("success")) {
                String transactionId = (String) gatewayResponse.get("transactionId");
                String gatewayResponseMsg = (String) gatewayResponse.get("gatewayResponse");
                
                payment.setStatus(Payment.PaymentStatus.SUCCESSFUL);
                payment.setTransactionId(transactionId);
                payment.setGatewayResponse(gatewayResponseMsg);
                payment.setProcessedAt(LocalDateTime.now());
                payment.setUpdatedAt(LocalDateTime.now());
                
                // Set card details if available
                if (paymentData.containsKey("cardLastFour")) {
                    payment.setCardLastFour((String) paymentData.get("cardLastFour"));
                }
                if (paymentData.containsKey("cardBrand")) {
                    payment.setCardBrand((String) paymentData.get("cardBrand"));
                }
                
                paymentRepository.save(payment);
                
                logger.info("Payment processed successfully: paymentId={}, transactionId={}", savedPayment.getId(), transactionId);
                
                return Map.of(
                    "success", true,
                    "paymentId", savedPayment.getId(),
                    "transactionId", transactionId,
                    "status", "SUCCESSFUL",
                    "amount", amount,
                    "gatewayResponse", gatewayResponseMsg
                );
                
            } else {
                String failureReason = (String) gatewayResponse.get("failureReason");
                String gatewayResponseMsg = (String) gatewayResponse.get("gatewayResponse");
                
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setGatewayResponse(gatewayResponseMsg);
                payment.setUpdatedAt(LocalDateTime.now());
                
                paymentRepository.save(payment);
                
                logger.warn("Payment failed: paymentId={}, reason={}", savedPayment.getId(), failureReason);
                
                return Map.of(
                    "success", false,
                    "paymentId", savedPayment.getId(),
                    "status", "FAILED",
                    "failureReason", failureReason,
                    "gatewayResponse", gatewayResponseMsg
                );
            }
            
        } catch (Exception e) {
            logger.error("Error processing payment for order {}: {}", orderId, e.getMessage(), e);
            throw new RuntimeException("Failed to process payment: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> verifyPayment(String transactionId, String orderId) {
        try {
            logger.info("Verifying payment: transactionId={}, orderId={}", transactionId, orderId);
            
            // Verify through gateway
            Map<String, Object> gatewayResponse = paymentGateway.verifyTransaction(transactionId);
            
            if ((Boolean) gatewayResponse.get("success")) {
                return Map.of(
                    "success", true,
                    "transactionId", transactionId,
                    "verified", true,
                    "status", "SUCCESSFUL"
                );
            } else {
                return Map.of(
                    "success", false,
                    "transactionId", transactionId,
                    "verified", false,
                    "error", gatewayResponse.get("error")
                );
            }
            
        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> processRefund(String paymentId, String orderId, BigDecimal amount, String reason) {
        try {
            logger.info("Processing refund: paymentId={}, orderId={}, amount={}, reason={}", paymentId, orderId, amount, reason);
            
            // Get payment record
        Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
            
            // Verify payment belongs to order
            if (!payment.getOrderId().equals(orderId)) {
                throw new RuntimeException("Payment does not belong to order: " + orderId);
            }
            
            // Process refund through gateway
            Map<String, Object> gatewayResponse = paymentGateway.processRefund(payment.getTransactionId(), amount, reason);
            
            if ((Boolean) gatewayResponse.get("success")) {
                String refundId = (String) gatewayResponse.get("refundId");
                
                // Update payment status
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                
                // Send refund event via Kafka
                kafkaProducerService.sendPaymentRefundedEvent(orderId, payment.getUserId(), amount, payment.getTransactionId(), refundId, reason);
                
                logger.info("Refund processed successfully: refundId={}, paymentId={}", refundId, paymentId);
                
                return Map.of(
                    "success", true,
                    "refundId", refundId,
                    "paymentId", paymentId,
                    "amount", amount,
                    "status", "REFUNDED"
                );
                
            } else {
                logger.warn("Refund failed: paymentId={}, error={}", paymentId, gatewayResponse.get("error"));
                
                return Map.of(
                    "success", false,
                    "paymentId", paymentId,
                    "error", gatewayResponse.get("error")
                );
            }
            
        } catch (Exception e) {
            logger.error("Error processing refund: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process refund: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getPaymentById(String paymentId, String userId) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
            
            // Verify user authorization
            if (!payment.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to view this payment");
            }
            
            return Map.of(
                "success", true,
                "payment", payment
            );
            
        } catch (Exception e) {
            logger.error("Error getting payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get payment: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getPaymentByOrderId(String orderId, String userId) {
        try {
            List<Payment> payments = paymentRepository.findByOrderId(orderId);
            if (payments.isEmpty()) {
                throw new RuntimeException("Payment not found for order: " + orderId);
            }
            
            Payment payment = payments.get(0); // Get the first payment for the order
            
            // Verify user authorization
            if (!payment.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to view this payment");
            }
            
            return Map.of(
                "success", true,
                "payment", payment
            );
            
        } catch (Exception e) {
            logger.error("Error getting payment by order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get payment: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUserPaymentHistory(String userId, int page, int size) {
        try {
            // Implementation for paginated payment history
            // This would use Spring Data JPA's Pageable interface
            return Map.of(
                "success", true,
                "message", "Payment history retrieved successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error getting user payment history: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get payment history: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> updatePaymentStatus(String paymentId, String status, String userId) {
        try {
        Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
            
            // Verify user authorization
            if (!payment.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to update this payment");
            }
            
            Payment.PaymentStatus newStatus = Payment.PaymentStatus.valueOf(status.toUpperCase());
            payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        
            logger.info("Payment status updated: {} -> {} for payment: {}", payment.getStatus(), newStatus, paymentId);
            
            return Map.of(
                "success", true,
                "paymentId", paymentId,
                "status", newStatus.toString(),
                "message", "Payment status updated successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error updating payment status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update payment status: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getGatewayStatus() {
        try {
            return paymentGateway.getGatewayStatus();
        } catch (Exception e) {
            logger.error("Error getting gateway status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get gateway status: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> testGatewayConnectivity() {
        try {
            return paymentGateway.testConnectivity();
        } catch (Exception e) {
            logger.error("Error testing gateway connectivity: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to test gateway connectivity: " + e.getMessage());
        }
    }
}
