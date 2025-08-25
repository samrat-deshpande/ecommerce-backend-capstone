package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing order operations
 * Provides endpoints for order creation, tracking, and management
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create a new order from cart
     * @param userId User ID (from authentication)
     * @param orderData Order data including delivery address and payment method
     * @return Response with created order details
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestParam String userId,
            @RequestBody Map<String, Object> orderData) {
        
        Map<String, Object> response = orderService.createOrder(userId, orderData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order by ID
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @return Order details
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.getOrderById(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get order by order number
     * @param orderNumber Order number
     * @param userId User ID (from authentication)
     * @return Order details
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Map<String, Object>> getOrderByNumber(
            @PathVariable String orderNumber,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.getOrderByNumber(orderNumber, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get user's order history with pagination
     * @param userId User ID (from authentication)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @return Paginated list of orders
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserOrders(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Map<String, Object> response = orderService.getUserOrders(userId, page, size);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get user's order history
     * @param userId User ID (from authentication)
     * @return List of user's orders
     */
    @GetMapping("/user/{userId}/all")
    public ResponseEntity<Map<String, Object>> getUserAllOrders(@PathVariable String userId) {
        Map<String, Object> response = orderService.getUserOrders(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get orders by status
     * @param userId User ID (from authentication)
     * @param status Order status
     * @return List of orders with specified status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        
        Map<String, Object> response = orderService.getOrdersByStatus(userId, status);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update order status
     * @param orderId Order ID
     * @param status New status
     * @param userId User ID (from authentication)
     * @return Response with updated order
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String status,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.updateOrderStatus(orderId, status, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Cancel order
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param request Request body with cancellation reason
     * @return Response with cancelled order
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> request) {
        
        String reason = (String) request.get("reason");
        
        Map<String, Object> response = orderService.cancelOrder(orderId, userId, reason);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order tracking information
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @return Order tracking details
     */
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<Map<String, Object>> getOrderTracking(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.getOrderTracking(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Track order by tracking number (public endpoint)
     * @param trackingNumber Tracking number
     * @return Order tracking details
     */
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> trackOrderByNumber(@PathVariable String trackingNumber) {
        Map<String, Object> response = orderService.trackOrderByNumber(trackingNumber);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update order tracking information
     * @param orderId Order ID
     * @param trackingData Tracking information
     * @param userId User ID (from authentication)
     * @return Response with updated tracking
     */
    @PutMapping("/{orderId}/tracking")
    public ResponseEntity<Map<String, Object>> updateOrderTracking(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> trackingData,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.updateOrderTracking(orderId, trackingData, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order confirmation details
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @return Order confirmation details
     */
    @GetMapping("/{orderId}/confirmation")
    public ResponseEntity<Map<String, Object>> getOrderConfirmation(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.getOrderConfirmation(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Resend order confirmation email
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @return Response with email status
     */
    @PostMapping("/{orderId}/resend-confirmation")
    public ResponseEntity<Map<String, Object>> resendOrderConfirmation(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.resendOrderConfirmation(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order statistics for user
     * @param userId User ID (from authentication)
     * @return Order statistics
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserOrderStats(@PathVariable String userId) {
        Map<String, Object> response = orderService.getUserOrderStats(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order details for admin (with full information)
     * @param orderId Order ID
     * @return Complete order details
     */
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderForAdmin(@PathVariable String orderId) {
        Map<String, Object> response = orderService.getOrderForAdmin(orderId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all orders with filtering and pagination (admin)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param status Order status filter
     * @param userId User ID filter
     * @param startDate Start date filter
     * @param endDate End date filter
     * @return Paginated list of orders
     */
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Map<String, Object> response = orderService.getAllOrders(page, size, status, userId, startDate, endDate);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update order delivery information
     * @param orderId Order ID
     * @param deliveryData Delivery information
     * @param userId User ID (from authentication)
     * @return Response with updated delivery info
     */
    @PutMapping("/{orderId}/delivery")
    public ResponseEntity<Map<String, Object>> updateOrderDelivery(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> deliveryData,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.updateOrderDelivery(orderId, userId, deliveryData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Request order refund
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @param refundData Refund request details
     * @return Response with refund request
     */
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Map<String, Object>> requestRefund(
            @PathVariable String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, Object> refundData) {
        
        Map<String, Object> response = orderService.requestRefund(orderId, userId, refundData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get order invoice
     * @param orderId Order ID
     * @param userId User ID (from authentication)
     * @return Order invoice details
     */
    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<Map<String, Object>> getOrderInvoice(
            @PathVariable String orderId,
            @RequestParam String userId) {
        
        Map<String, Object> response = orderService.getOrderInvoice(orderId, userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
