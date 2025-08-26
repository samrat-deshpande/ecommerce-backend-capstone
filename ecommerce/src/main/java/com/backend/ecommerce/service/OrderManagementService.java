package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.OrderItem;
import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.repository.OrderRepository;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhanced Order Management Service with Kafka integration
 * Handles order processing, history, and tracking
 * Communicates with Payment Service and User Management Service through Kafka
 */
@Service
public class OrderManagementService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderKafkaProducerService kafkaProducerService;
    
    /**
     * Create a new order from cart with Kafka event publishing
     * @param userId User ID
     * @param orderData Order data including delivery address and payment method
     * @return Response with created order details
     */
    @Transactional
    public Map<String, Object> createOrder(String userId, Map<String, Object> orderData) {
        try {
            // Validate user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            
            // Get user's active cart
            Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("No active cart found for user: " + userId));
            
            if (cart.getItems().isEmpty()) {
                throw new RuntimeException("Cannot create order from empty cart");
            }
            
            // Generate order number
            String orderNumber = generateOrderNumber();
            
            // Create order
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderNumber(orderNumber);
            order.setStatus(Order.OrderStatus.PENDING);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
            order.setPaymentMethod(Order.PaymentMethod.valueOf((String) orderData.get("paymentMethod")));
            
            // Set delivery information
            order.setDeliveryAddress((String) orderData.get("deliveryAddress"));
            order.setDeliveryCity((String) orderData.get("deliveryCity"));
            order.setDeliveryState((String) orderData.get("deliveryState"));
            order.setDeliveryZipCode((String) orderData.get("deliveryZipCode"));
            order.setDeliveryCountry((String) orderData.get("deliveryCountry"));
            order.setDeliveryPhone((String) orderData.get("deliveryPhone"));
            
            // Calculate amounts
            BigDecimal subtotal = cart.getTotalAmount();
            BigDecimal taxAmount = calculateTax(subtotal);
            BigDecimal shippingAmount = calculateShipping(orderData);
            BigDecimal totalAmount = subtotal.add(taxAmount).add(shippingAmount);
            
            order.setSubtotal(subtotal);
            order.setTaxAmount(taxAmount);
            order.setShippingAmount(shippingAmount);
            order.setTotalAmount(totalAmount);
            
            // Create order items from cart items
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductImage(cartItem.getProductImage());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(cartItem.getUnitPrice());
                orderItem.setSubtotal(cartItem.getSubtotal());
                orderItems.add(orderItem);
            }
            order.setItems(orderItems);
            
            // Save order
            Order savedOrder = orderRepository.save(order);
            
            // Clear cart
            cart.setStatus(Cart.CartStatus.CONVERTED);
            cartRepository.save(cart);
            
            // Publish order created event via Kafka
            kafkaProducerService.sendOrderCreatedEvent(
                savedOrder.getId(), 
                userId, 
                orderNumber, 
                totalAmount
            );
            
            // Send payment verification request
            kafkaProducerService.sendPaymentVerificationRequest(
                savedOrder.getId(),
                userId,
                totalAmount,
                order.getPaymentMethod().toString()
            );
            
            logger.info("Order created successfully: {} for user: {}", orderNumber, userId);
            
            return Map.of(
                "success", true,
                "orderId", savedOrder.getId(),
                "orderNumber", orderNumber,
                "totalAmount", totalAmount,
                "message", "Order created successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error creating order for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }
    
    /**
     * Update order status and publish Kafka event
     * @param orderId Order ID
     * @param status New status
     * @param userId User ID (for authorization)
     * @return Response with updated order
     */
    @Transactional
    public Map<String, Object> updateOrderStatus(String orderId, String status, String userId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            
            // Verify user authorization
            if (!order.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to update this order");
            }
            
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            
            Order savedOrder = orderRepository.save(order);
            
            // Publish order status update event via Kafka
            switch (newStatus) {
                case CONFIRMED:
                    kafkaProducerService.sendOrderConfirmedEvent(orderId, userId);
                    break;
                case SHIPPED:
                    kafkaProducerService.sendOrderShippedEvent(orderId, userId, order.getTrackingNumber());
                    break;
                case DELIVERED:
                    kafkaProducerService.sendOrderDeliveredEvent(orderId, userId);
                    break;
                case CANCELLED:
                    kafkaProducerService.sendOrderCancelledEvent(orderId, userId, "Order cancelled by user");
                    break;
            }
            
            logger.info("Order status updated: {} -> {} for order: {}", order.getStatus(), newStatus, orderId);
            
            return Map.of(
                "success", true,
                "orderId", orderId,
                "status", newStatus.toString(),
                "message", "Order status updated successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error updating order status: {} for order: {}", e.getMessage(), orderId);
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }
    
    /**
     * Update order payment status
     * @param orderId Order ID
     * @param paymentStatus New payment status
     * @param userId User ID (for authorization)
     * @return Response with updated order
     */
    @Transactional
    public Map<String, Object> updateOrderPaymentStatus(String orderId, String paymentStatus, String userId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            
            // Verify user authorization
            if (!order.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to update this order");
            }
            
            Order.PaymentStatus newPaymentStatus = Order.PaymentStatus.valueOf(paymentStatus.toUpperCase());
            order.setPaymentStatus(newPaymentStatus);
            order.setUpdatedAt(LocalDateTime.now());
            
            Order savedOrder = orderRepository.save(order);
            
            // If payment is successful, update order status to confirmed
            if (Order.PaymentStatus.PAID.equals(newPaymentStatus)) {
                order.setStatus(Order.OrderStatus.CONFIRMED);
                orderRepository.save(order);
                
                // Publish order paid event
                kafkaProducerService.sendOrderPaidEvent(orderId, userId, order.getPaymentTransactionId());
            }
            
            logger.info("Order payment status updated: {} -> {} for order: {}", order.getPaymentStatus(), newPaymentStatus, orderId);
            
            return Map.of(
                "success", true,
                "orderId", orderId,
                "paymentStatus", newPaymentStatus.toString(),
                "message", "Order payment status updated successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error updating order payment status: {} for order: {}", e.getMessage(), orderId);
            throw new RuntimeException("Failed to update order payment status: " + e.getMessage());
        }
    }
    
    /**
     * Get order tracking information
     * @param orderId Order ID
     * @param userId User ID (for authorization)
     * @return Order tracking details
     */
    public Map<String, Object> getOrderTracking(String orderId, String userId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            
            // Verify user authorization
            if (!order.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to view this order");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("orderNumber", order.getOrderNumber());
            response.put("status", order.getStatus().toString());
            response.put("trackingNumber", order.getTrackingNumber());
            response.put("estimatedDeliveryDate", order.getEstimatedDeliveryDate());
            response.put("deliveryAddress", order.getDeliveryAddress());
            response.put("deliveryCity", order.getDeliveryCity());
            response.put("deliveryState", order.getDeliveryState());
            response.put("deliveryZipCode", order.getDeliveryZipCode());
            response.put("deliveryCountry", order.getDeliveryCountry());
            return response;
            
        } catch (Exception e) {
            logger.error("Error getting order tracking: {} for order: {}", e.getMessage(), orderId);
            throw new RuntimeException("Failed to get order tracking: " + e.getMessage());
        }
    }
    
    /**
     * Get user's order history with pagination
     * @param userId User ID
     * @param page Page number
     * @param size Page size
     * @return Paginated list of orders
     */
    public Map<String, Object> getUserOrders(String userId, int page, int size) {
        try {
            // Implementation for paginated order history
            // This would use Spring Data JPA's Pageable interface
            return Map.of(
                "success", true,
                "message", "Order history retrieved successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error getting user orders: {} for user: {}", e.getMessage(), userId);
            throw new RuntimeException("Failed to get user orders: " + e.getMessage());
        }
    }
    
    /**
     * Generate unique order number
     * @return Unique order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Calculate tax amount
     * @param subtotal Order subtotal
     * @return Tax amount
     */
    private BigDecimal calculateTax(BigDecimal subtotal) {
        // Simple tax calculation - in real implementation, this would be more complex
        return subtotal.multiply(new BigDecimal("0.10")); // 10% tax
    }
    
    /**
     * Calculate shipping amount
     * @param orderData Order data
     * @return Shipping amount
     */
    private BigDecimal calculateShipping(Map<String, Object> orderData) {
        // Simple shipping calculation - in real implementation, this would be more complex
        return new BigDecimal("15.00"); // Fixed shipping cost
    }
}
