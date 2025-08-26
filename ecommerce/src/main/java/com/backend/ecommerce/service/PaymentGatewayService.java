package com.backend.ecommerce.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service for managing different payment gateways
 */
public interface PaymentGatewayService {
    
    /**
     * Process payment through the gateway
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @param paymentData Payment data (card details, etc.)
     * @return Gateway response
     */
    Map<String, Object> processPayment(BigDecimal amount, String paymentMethod, Map<String, Object> paymentData);
    
    /**
     * Verify payment transaction
     * @param transactionId Transaction ID from gateway
     * @return Verification result
     */
    Map<String, Object> verifyTransaction(String transactionId);
    
    /**
     * Process refund through the gateway
     * @param transactionId Original transaction ID
     * @param amount Refund amount
     * @param reason Refund reason
     * @return Refund result
     */
    Map<String, Object> processRefund(String transactionId, BigDecimal amount, String reason);
    
    /**
     * Get gateway status
     * @return Gateway status information
     */
    Map<String, Object> getGatewayStatus();
    
    /**
     * Test gateway connectivity
     * @return Connectivity test result
     */
    Map<String, Object> testConnectivity();
    
    /**
     * Get supported payment methods
     * @return List of supported payment methods
     */
    Map<String, Object> getSupportedPaymentMethods();
}
