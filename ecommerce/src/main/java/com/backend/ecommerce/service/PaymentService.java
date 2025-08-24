package com.backend.ecommerce.service;

import java.util.Map;

/**
 * Service interface for managing payment operations
 */
public interface PaymentService {
    
    /**
     * Process payment for an order
     * @param orderId Order ID
     * @param userId User ID
     * @param paymentData Payment information including method and details
     * @return Response with payment processing result
     */
    Map<String, Object> processPayment(String orderId, String userId, Map<String, Object> paymentData);
    
    /**
     * Process credit card payment
     * @param orderId Order ID
     * @param userId User ID
     * @param cardData Credit card information
     * @return Response with payment processing result
     */
    Map<String, Object> processCreditCardPayment(String orderId, String userId, Map<String, Object> cardData);
    
    /**
     * Process digital wallet payment
     * @param orderId Order ID
     * @param userId User ID
     * @param walletData Digital wallet information
     * @return Response with payment processing result
     */
    Map<String, Object> processDigitalWalletPayment(String orderId, String userId, Map<String, Object> walletData);
    
    /**
     * Process bank transfer payment
     * @param orderId Order ID
     * @param userId User ID
     * @param bankData Bank transfer information
     * @return Response with payment processing result
     */
    Map<String, Object> processBankTransferPayment(String orderId, String userId, Map<String, Object> bankData);
    
    /**
     * Get payment details by ID
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @return Payment details
     */
    Map<String, Object> getPaymentById(String paymentId, String userId);
    
    /**
     * Get payment by transaction ID
     * @param transactionId Transaction ID
     * @return Payment details
     */
    Map<String, Object> getPaymentByTransactionId(String transactionId);
    
    /**
     * Get payment history for user
     * @param userId User ID
     * @param page Page number
     * @param size Page size
     * @return Paginated list of payments
     */
    Map<String, Object> getUserPaymentHistory(String userId, int page, int size);
    
    /**
     * Get payment history for user
     * @param userId User ID
     * @return List of user's payments
     */
    Map<String, Object> getUserPaymentHistory(String userId);
    
    /**
     * Get payments by status
     * @param userId User ID
     * @param status Payment status
     * @return List of payments with specified status
     */
    Map<String, Object> getPaymentsByStatus(String userId, String status);
    
    /**
     * Get payments by payment method
     * @param userId User ID
     * @param paymentMethod Payment method
     * @return List of payments with specified method
     */
    Map<String, Object> getPaymentsByMethod(String userId, String paymentMethod);
    
    /**
     * Refund payment
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @param refundData Refund details
     * @return Response with refund result
     */
    Map<String, Object> refundPayment(String paymentId, String userId, Map<String, Object> refundData);
    
    /**
     * Partial refund payment
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @param amount Refund amount
     * @param reason Refund reason
     * @return Response with partial refund result
     */
    Map<String, Object> partialRefundPayment(String paymentId, String userId, Double amount, String reason);
    
    /**
     * Cancel payment
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @param reason Cancellation reason
     * @return Response with cancellation result
     */
    Map<String, Object> cancelPayment(String paymentId, String userId, String reason);
    
    /**
     * Validate payment method
     * @param paymentMethod Payment method
     * @param paymentData Payment data to validate
     * @return Validation result
     */
    Map<String, Object> validatePaymentMethod(String paymentMethod, Map<String, Object> paymentData);
    
    /**
     * Validate credit card
     * @param cardData Credit card information
     * @return Validation result
     */
    Map<String, Object> validateCreditCard(Map<String, Object> cardData);
    
    /**
     * Get payment receipt
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @return Payment receipt details
     */
    Map<String, Object> getPaymentReceipt(String paymentId, String userId);
    
    /**
     * Resend payment receipt
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @return Response with email status
     */
    Map<String, Object> resendPaymentReceipt(String paymentId, String userId);
    
    /**
     * Get payment statistics for user
     * @param userId User ID
     * @return Payment statistics
     */
    Map<String, Object> getUserPaymentStats(String userId);
    
    /**
     * Get payment details for admin (with full information)
     * @param paymentId Payment ID
     * @return Complete payment details
     */
    Map<String, Object> getPaymentForAdmin(String paymentId);
    
    /**
     * Get all payments with filtering and pagination (admin)
     * @param page Page number
     * @param size Page size
     * @param status Payment status filter
     * @param userId User ID filter
     * @param paymentMethod Payment method filter
     * @param startDate Start date filter
     * @param endDate End date filter
     * @return Paginated list of payments
     */
    Map<String, Object> getAllPayments(int page, int size, String status, String userId, String paymentMethod, String startDate, String endDate);
    
    /**
     * Retry failed payment
     * @param paymentId Payment ID
     * @param userId User ID (for authorization)
     * @return Response with retry result
     */
    Map<String, Object> retryFailedPayment(String paymentId, String userId);
    
    /**
     * Get available payment methods
     * @return List of available payment methods
     */
    Map<String, Object> getAvailablePaymentMethods();
    
    /**
     * Check payment gateway status
     * @return Gateway status information
     */
    Map<String, Object> checkPaymentGatewayStatus();
    
    /**
     * Generate payment transaction ID
     * @return Unique transaction ID
     */
    String generateTransactionId();
    
    /**
     * Calculate payment fees
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return Calculated fees
     */
    Map<String, Object> calculatePaymentFees(Double amount, String paymentMethod);
}
