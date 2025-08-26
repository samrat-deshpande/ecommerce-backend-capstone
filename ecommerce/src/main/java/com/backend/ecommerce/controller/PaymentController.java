package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestParam String orderId,
            @RequestParam String userId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod,
            @RequestBody Map<String, Object> paymentData) {
        
        Map<String, Object> response = paymentService.processPayment(orderId, userId, amount, paymentMethod, paymentData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Verify payment transaction
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestParam String transactionId,
            @RequestParam String orderId) {
        
        Map<String, Object> response = paymentService.verifyPayment(transactionId, orderId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment details by ID
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
     * Get payment by order ID
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getPaymentByOrderId(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.getPaymentByOrderId(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get payment history for user with pagination
     */
    @GetMapping("/user/{userId}/history")
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
     * Process refund for a payment
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Map<String, Object>> processRefund(
            @PathVariable String paymentId,
            @RequestParam String userId,
            @RequestParam BigDecimal amount,
            @RequestParam String reason) {
        
        Map<String, Object> response = paymentService.processRefund(paymentId, userId, amount, reason);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update payment status
     */
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(
            @PathVariable String paymentId,
            @RequestParam String status,
            @RequestParam String userId) {
        
        Map<String, Object> response = paymentService.updatePaymentStatus(paymentId, status, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get payment gateway status
     */
    @GetMapping("/gateway/status")
    public ResponseEntity<Map<String, Object>> getGatewayStatus() {
        Map<String, Object> response = paymentService.getGatewayStatus();
        return ResponseEntity.ok(response);
    }

    /**
     * Test payment gateway connectivity
     */
    @GetMapping("/gateway/test")
    public ResponseEntity<Map<String, Object>> testGatewayConnectivity() {
        Map<String, Object> response = paymentService.testGatewayConnectivity();
        return ResponseEntity.ok(response);
    }
}
