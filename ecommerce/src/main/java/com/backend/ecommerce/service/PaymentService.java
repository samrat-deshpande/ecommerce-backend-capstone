package com.backend.ecommerce.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service interface for managing payment operations
 */
public interface PaymentService {
    
    /**
     * Process payment for an order
     * @param orderId Order ID
     * @param userId User ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @param paymentData Additional payment data (card details, etc.)
     * @return Response with payment details
     */
    Map<String, Object> processPayment(String orderId, String userId, BigDecimal amount, String paymentMethod, Map<String, Object> paymentData);
    
    /**
     * Verify payment transaction
     * @param transactionId Transaction ID
     * @param orderId Order ID
     * @return Payment verification result
     */
    Map<String, Object> verifyPayment(String transactionId, String orderId);
    
    /**
     * Process refund for a payment
     * @param paymentId Payment ID
     * @param orderId Order ID
     * @param amount Refund amount
     * @param reason Refund reason
     * @return Refund result
     */
    Map<String, Object> processRefund(String paymentId, String orderId, BigDecimal amount, String reason);
    
    /**
     * Get payment details by ID
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @return Payment details
     */
    Map<String, Object> getPaymentById(String paymentId, String userId);
    
    /**
     * Get payment details by order ID
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Payment details
     */
    Map<String, Object> getPaymentByOrderId(String orderId, String userId);
    
    /**
     * Get payment history for a user
     * @param userId User ID
     * @param page Page number
     * @param size Page size
     * @return Paginated payment history
     */
    Map<String, Object> getUserPaymentHistory(String userId, int page, int size);
    
    /**
     * Update payment status
     * @param paymentId Payment ID
     * @param status New status
     * @param userId User ID (for authorization)
     * @return Updated payment details
     */
    Map<String, Object> updatePaymentStatus(String paymentId, String status, String userId);
    
    /**
     * Get payment gateway status
     * @return Gateway status information
     */
    Map<String, Object> getGatewayStatus();
    
    /**
     * Test payment gateway connectivity
     * @return Connectivity test result
     */
    Map<String, Object> testGatewayConnectivity();
}
