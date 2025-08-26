package com.backend.ecommerce.service;

import java.util.Map;

/**
 * Service interface for managing order operations
 */
public interface OrderService {
    
    /**
     * Create a new order from cart
     * @param userId User ID
     * @param orderData Order data including delivery address and payment method
     * @return Response with created order details
     */
    Map<String, Object> createOrder(String userId, Map<String, Object> orderData);
    
    /**
     * Get order by ID
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Order details
     */
    Map<String, Object> getOrderById(String orderId, String userId);
    
    /**
     * Get order by order number
     * @param orderNumber Order number
     * @param userId User ID (for authorization)
     * @return Order details
     */
    Map<String, Object> getOrderByNumber(String orderNumber, String userId);
    
    /**
     * Get user's order history with pagination
     * @param userId User ID
     * @param page Page number
     * @param size Page size
     * @return Paginated list of orders
     */
    Map<String, Object> getUserOrders(String userId, int page, int size);
    
    /**
     * Get user's order history
     * @param userId User ID
     * @return List of user's orders
     */
    Map<String, Object> getUserOrders(String userId);
    
    /**
     * Get orders by status
     * @param userId User ID
     * @param status Order status
     * @return List of orders with specified status
     */
    Map<String, Object> getOrdersByStatus(String userId, String status);
    
    /**
     * Update order status
     * @param orderId Order ID
     * @param status New status
     * @param userId User ID (for authorization)
     * @return Response with updated order
     */
    Map<String, Object> updateOrderStatus(String orderId, String status, String userId);
    
    /**
     * Cancel order
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @param reason Cancellation reason
     * @return Response with cancelled order
     */
    Map<String, Object> cancelOrder(String orderId, String userId, String reason);
    
    /**
     * Get order tracking information
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Order tracking details
     */
    Map<String, Object> getOrderTracking(String orderId, String userId);
    
    /**
     * Track order by tracking number
     * @param trackingNumber Tracking number
     * @return Order tracking details
     */
    Map<String, Object> trackOrderByNumber(String trackingNumber);
    
    /**
     * Update order tracking information
     * @param orderId Order ID
     * @param trackingData Tracking information
     * @param userId User ID (for authorization)
     * @return Response with updated tracking
     */
    Map<String, Object> updateOrderTracking(String orderId, Map<String, Object> trackingData, String userId);
    
    /**
     * Update order payment status
     * @param orderId Order ID
     * @param paymentStatus New payment status
     * @param userId User ID (for authorization)
     * @return Response with updated order
     */
    Map<String, Object> updateOrderPaymentStatus(String orderId, String paymentStatus, String userId);
    
    /**
     * Get order confirmation details
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Order confirmation details
     */
    Map<String, Object> getOrderConfirmation(String orderId, String userId);
    
    /**
     * Resend order confirmation email
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Response with email status
     */
    Map<String, Object> resendOrderConfirmation(String orderId, String userId);
    
    /**
     * Get order statistics for user
     * @param userId User ID
     * @return Order statistics
     */
    Map<String, Object> getUserOrderStats(String userId);
    
    /**
     * Get order details for admin (with full information)
     * @param orderId Order ID
     * @return Complete order details
     */
    Map<String, Object> getOrderForAdmin(String orderId);
    
    /**
     * Get all orders with filtering and pagination (admin)
     * @param page Page number
     * @param size Page size
     * @param status Order status filter
     * @param userId User ID filter
     * @param startDate Start date filter
     * @param endDate End date filter
     * @return Paginated list of orders
     */
    Map<String, Object> getAllOrders(int page, int size, String status, String userId, String startDate, String endDate);
    
    /**
     * Update order delivery information
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @param deliveryData Delivery information
     * @return Response with updated delivery info
     */
    Map<String, Object> updateOrderDelivery(String orderId, String userId, Map<String, Object> deliveryData);
    
    /**
     * Request order refund
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @param refundData Refund request details
     * @return Response with refund request
     */
    Map<String, Object> requestRefund(String orderId, String userId, Map<String, Object> refundData);
    
    /**
     * Get order invoice
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Order invoice details
     */
    Map<String, Object> getOrderInvoice(String orderId, String userId);
    
    /**
     * Generate order number
     * @return Unique order number
     */
    String generateOrderNumber();
    
    /**
     * Calculate order totals (subtotal, tax, shipping, total)
     * @param orderId Order ID
     * @return Calculated totals
     */
    Map<String, Object> calculateOrderTotals(String orderId);
    
    /**
     * Create order with items and delivery details
     * @param userId User ID
     * @param items List of order items
     * @param deliveryAddress Delivery address
     * @param paymentMethod Payment method
     * @return Created order
     */
    Map<String, Object> createOrder(String userId, Object items, String deliveryAddress, String paymentMethod);
    
    /**
     * Update order tracking with tracking number and estimated delivery
     * @param orderId Order ID
     * @param trackingNumber Tracking number
     * @param estimatedDelivery Estimated delivery date
     * @return Updated order
     */
    Map<String, Object> updateOrderTracking(String orderId, String trackingNumber, Object estimatedDelivery);
    
    /**
     * Update order delivery with tracking number and estimated delivery
     * @param orderId Order ID
     * @param trackingNumber Tracking number
     * @param estimatedDelivery Estimated delivery date
     * @return Updated order
     */
    Map<String, Object> updateOrderDelivery(String orderId, String trackingNumber, Object estimatedDelivery);
}
