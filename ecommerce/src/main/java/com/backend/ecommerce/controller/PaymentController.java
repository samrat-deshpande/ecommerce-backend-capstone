package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing payment operations
 * Provides endpoints for payment processing, refunds, and payment management
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Process payment for an order
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param paymentData Payment information including method and details
     * @return Response with payment processing result
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> paymentData) {
        
        Map<String, Object> response = paymentService.processPayment(orderId, userId, paymentData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Process credit card payment
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param cardData Credit card information
     * @return Response with payment processing result
     */
    @PostMapping("/credit-card")
    public ResponseEntity<Map<String, Object>> processCreditCardPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> cardData) {
        
        Map<String, Object> response = paymentService.processCreditCardPayment(orderId, userId, cardData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Process digital wallet payment
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param walletData Digital wallet information
     * @return Response with payment processing result
     */
    @PostMapping("/digital-wallet")
    public ResponseEntity<Map<String, Object>> processDigitalWalletPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> walletData) {
        
        Map<String, Object> response = paymentService.processDigitalWalletPayment(orderId, userId, walletData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Process bank transfer payment
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param bankData Bank transfer information
     * @return Response with payment processing result
     */
    @PostMapping("/bank-transfer")
    public ResponseEntity<Map<String, Object>> processBankTransferPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> bankData) {
        
        Map<String, Object> response = paymentService.processBankTransferPayment(orderId, userId, bankData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment details by ID
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @return Payment details
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<Map<String, Object>> getPaymentById(
            @PathVariable String paymentId,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.getPaymentById(paymentId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get payment by transaction ID
     * @param transactionId Transaction ID
     * @return Payment details
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Map<String, Object>> getPaymentByTransactionId(@PathVariable String transactionId) {
        Map<String, Object> response = paymentService.getPaymentByTransactionId(transactionId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get payment history for user with pagination
     * @param userId User ID (from authentication)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Paginated list of payments
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPaymentHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = paymentService.getUserPaymentHistory(userId, page, size);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment history for user
     * @param userId User ID (from authentication)
     * @return List of user's payments
     */
    @GetMapping("/user/{userId}/all")
    public ResponseEntity<Map<String, Object>> getUserAllPayments(@PathVariable String userId) {
        Map<String, Object> response = paymentService.getUserPaymentHistory(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payments by status
     * @param userId User ID (from authentication)
     * @param status Payment status
     * @return List of payments with specified status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Map<String, Object>> getPaymentsByStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        
        Map<String, Object> response = paymentService.getPaymentsByStatus(userId, status);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payments by payment method
     * @param userId User ID (from authentication)
     * @param paymentMethod Payment method
     * @return List of payments with specified method
     */
    @GetMapping("/user/{userId}/method/{paymentMethod}")
    public ResponseEntity<Map<String, Object>> getPaymentsByMethod(
            @PathVariable String userId,
            @PathVariable String paymentMethod) {
        
        Map<String, Object> response = paymentService.getPaymentsByMethod(userId, paymentMethod);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Refund payment
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @param refundData Refund details
     * @return Response with refund result
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Map<String, Object>> refundPayment(
            @PathVariable String paymentId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> refundData) {
        
        Map<String, Object> response = paymentService.refundPayment(paymentId, userId, refundData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Partial refund payment
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @param request Request body with amount and reason
     * @return Response with partial refund result
     */
    @PostMapping("/{paymentId}/partial-refund")
    public ResponseEntity<Map<String, Object>> partialRefundPayment(
            @PathVariable String paymentId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> request) {
        
        Double amount = (Double) request.get("amount");
        String reason = (String) request.get("reason");
        
        if (amount == null || amount <= 0) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Invalid refund amount"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Map<String, Object> response = paymentService.partialRefundPayment(paymentId, userId, amount, reason);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Cancel payment
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @param request Request body with cancellation reason
     * @return Response with cancellation result
     */
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelPayment(
            @PathVariable String paymentId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> request) {
        
        String reason = (String) request.get("reason");
        
        Map<String, Object> response = paymentService.cancelPayment(paymentId, userId, reason);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Validate payment method
     * @param paymentMethod Payment method
     * @param paymentData Payment data to validate
     * @return Validation result
     */
    @PostMapping("/validate/{paymentMethod}")
    public ResponseEntity<Map<String, Object>> validatePaymentMethod(
            @PathVariable String paymentMethod,
            @RequestBody Map<String, Object> paymentData) {
        
        Map<String, Object> response = paymentService.validatePaymentMethod(paymentMethod, paymentData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Validate credit card
     * @param cardData Credit card information
     * @return Validation result
     */
    @PostMapping("/validate/credit-card")
    public ResponseEntity<Map<String, Object>> validateCreditCard(@RequestBody Map<String, Object> cardData) {
        Map<String, Object> response = paymentService.validateCreditCard(cardData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment receipt
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @return Payment receipt details
     */
    @GetMapping("/{paymentId}/receipt")
    public ResponseEntity<Map<String, Object>> getPaymentReceipt(
            @PathVariable String paymentId,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.getPaymentReceipt(paymentId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Resend payment receipt
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @return Response with email status
     */
    @PostMapping("/{paymentId}/resend-receipt")
    public ResponseEntity<Map<String, Object>> resendPaymentReceipt(
            @PathVariable String paymentId,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.resendPaymentReceipt(paymentId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment statistics for user
     * @param userId User ID (from authentication)
     * @return Payment statistics
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserPaymentStats(@PathVariable String userId) {
        Map<String, Object> response = paymentService.getUserPaymentStats(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment details for admin (with full information)
     * @param paymentId Payment ID
     * @return Complete payment details
     */
    @GetMapping("/admin/{paymentId}")
    public ResponseEntity<Map<String, Object>> getPaymentForAdmin(@PathVariable String paymentId) {
        Map<String, Object> response = paymentService.getPaymentForAdmin(paymentId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all payments with filtering and pagination (admin)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param status Payment status filter
     * @param userId User ID filter
     * @param paymentMethod Payment method filter
     * @param startDate Start date filter
     * @param endDate End date filter
     * @return Paginated list of payments
     */
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Map<String, Object> response = paymentService.getAllPayments(page, size, status, userId, paymentMethod, startDate, endDate);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Retry failed payment
     * @param paymentId Payment ID
     * @param userId User ID (from authentication)
     * @return Response with retry result
     */
    @PostMapping("/{paymentId}/retry")
    public ResponseEntity<Map<String, Object>> retryFailedPayment(
            @PathVariable String paymentId,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.retryFailedPayment(paymentId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get available payment methods
     * @return List of available payment methods
     */
    @GetMapping("/methods")
    public ResponseEntity<Map<String, Object>> getAvailablePaymentMethods() {
        Map<String, Object> response = paymentService.getAvailablePaymentMethods();
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Check payment gateway status
     * @return Gateway status information
     */
    @GetMapping("/gateway/status")
    public ResponseEntity<Map<String, Object>> checkPaymentGatewayStatus() {
        Map<String, Object> response = paymentService.checkPaymentGatewayStatus();
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Calculate payment fees
     * @param amount Payment amount
     * @param paymentMethod Payment method
     * @return Calculated fees
     */
    @GetMapping("/fees")
    public ResponseEntity<Map<String, Object>> calculatePaymentFees(
            @RequestParam Double amount,
            @RequestParam String paymentMethod) {
        
        Map<String, Object> response = paymentService.calculatePaymentFees(amount, paymentMethod);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
