package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.repository.OrderRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Map<String, Object> createOrder(String userId, Map<String, Object> orderData) {
        // Extract data from orderData
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
        String deliveryAddress = (String) orderData.get("deliveryAddress");
        String paymentMethod = (String) orderData.get("paymentMethod");

        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create order
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(userId);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setPaymentMethod(Order.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        order.setDeliveryAddress(deliveryAddress);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Create order items and calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map<String, Object> itemData : itemsData) {
            String productId = (String) itemData.get("productId");
            Integer quantity = (Integer) itemData.get("quantity");

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setOrder(order);
            orderItem.setProductId(productId);
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImageUrl());
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());

            order.getItems().add(orderItem);
            subtotal = subtotal.add(orderItem.getSubtotal());

            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
        }

        // Calculate order totals
        order.setSubtotal(subtotal);
        order.setTaxAmount(calculateTaxAmount(subtotal));
        order.setShippingAmount(calculateShippingAmount(subtotal));
        order.setTotalAmount(calculateTotalAmount(order.getSubtotal(), order.getTaxAmount(), order.getShippingAmount()));

        Order savedOrder = orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getOrderById(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getOrderByNumber(String orderNumber, String userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getUserOrders(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersPage.getContent());
        response.put("totalPages", ordersPage.getTotalPages());
        response.put("totalElements", ordersPage.getTotalElements());
        response.put("currentPage", page);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getOrdersByStatus(String userId, String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        List<Order> orders = orderRepository.findByStatus(orderStatus);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> updateOrderStatus(String orderId, String status, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> cancelOrder(String orderId, String userId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }

        if (order.getStatus() == Order.OrderStatus.SHIPPED || 
            order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel shipped or delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        // Restore product stock
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        Order savedOrder = orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getOrderTracking(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> tracking = new HashMap<>();
        tracking.put("orderId", orderId);
        tracking.put("orderNumber", order.getOrderNumber());
        tracking.put("status", order.getStatus());
        tracking.put("trackingNumber", order.getTrackingNumber());
        tracking.put("estimatedDelivery", order.getEstimatedDeliveryDate());
        tracking.put("lastUpdated", order.getUpdatedAt());
        tracking.put("success", true);
        
        return tracking;
    }

    @Override
    public Map<String, Object> trackOrderByNumber(String trackingNumber) {
        Order order = orderRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> updateOrderTracking(String orderId, Map<String, Object> trackingData, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        String trackingNumber = (String) trackingData.get("trackingNumber");
        LocalDateTime estimatedDelivery = (LocalDateTime) trackingData.get("estimatedDelivery");
        
        order.setTrackingNumber(trackingNumber);
        order.setEstimatedDeliveryDate(estimatedDelivery);
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getOrderConfirmation(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> confirmation = new HashMap<>();
        confirmation.put("orderId", orderId);
        confirmation.put("orderNumber", order.getOrderNumber());
        confirmation.put("orderDate", order.getCreatedAt());
        confirmation.put("totalAmount", order.getTotalAmount());
        confirmation.put("status", order.getStatus());
        confirmation.put("items", order.getItems());
        confirmation.put("success", true);
        
        return confirmation;
    }

    @Override
    public Map<String, Object> resendOrderConfirmation(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // TODO: Implement email sending logic
        // This would typically involve:
        // 1. Getting user email from order
        // 2. Sending confirmation email
        // 3. Logging the action
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order confirmation email sent");
        return response;
    }

    @Override
    public Map<String, Object> getUserOrderStats(String userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", userOrders.size());
        stats.put("totalSpent", userOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("averageOrderValue", userOrders.isEmpty() ? BigDecimal.ZERO :
                stats.get("totalSpent").toString().equals("0") ? BigDecimal.ZERO :
                ((BigDecimal) stats.get("totalSpent")).divide(BigDecimal.valueOf(userOrders.size()), 2, RoundingMode.HALF_UP));
        stats.put("success", true);
        
        return stats;
    }

    @Override
    public Map<String, Object> getOrderForAdmin(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getAllOrders(int page, int size, String status, String userId, String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orders", ordersPage.getContent());
        response.put("totalPages", ordersPage.getTotalPages());
        response.put("totalElements", ordersPage.getTotalElements());
        response.put("currentPage", page);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> updateOrderDelivery(String orderId, String userId, Map<String, Object> deliveryData) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Update delivery information
        if (deliveryData.containsKey("deliveryAddress")) {
            order.setDeliveryAddress((String) deliveryData.get("deliveryAddress"));
        }
        if (deliveryData.containsKey("deliveryCity")) {
            order.setDeliveryCity((String) deliveryData.get("deliveryCity"));
        }
        if (deliveryData.containsKey("deliveryState")) {
            order.setDeliveryState((String) deliveryData.get("deliveryState"));
        }
        if (deliveryData.containsKey("deliveryCountry")) {
            order.setDeliveryCountry((String) deliveryData.get("deliveryCountry"));
        }
        if (deliveryData.containsKey("deliveryPhone")) {
            order.setDeliveryPhone((String) deliveryData.get("deliveryPhone"));
        }
        
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> requestRefund(String orderId, String userId, Map<String, Object> refundData) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Only delivered orders can be refunded");
        }

        // TODO: Implement refund request logic
        // This would typically involve:
        // 1. Creating a refund request record
        // 2. Notifying customer service
        // 3. Updating order status
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Refund request submitted");
        return response;
    }

    @Override
    public Map<String, Object> getOrderInvoice(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // TODO: Generate invoice PDF or HTML
        // This would typically involve:
        // 1. Creating invoice template
        // 2. Populating with order data
        // 3. Generating PDF/HTML output
        
        Map<String, Object> response = new HashMap<>();
        response.put("invoice", "Invoice for Order: " + order.getOrderNumber());
        response.put("success", true);
        return response;
    }

    @Override
    public String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + timestamp + "-" + random;
    }

    @Override
    public Map<String, Object> calculateOrderTotals(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Map<String, Object> totals = new HashMap<>();
        totals.put("subtotal", order.getSubtotal());
        totals.put("taxAmount", order.getTaxAmount());
        totals.put("shippingAmount", order.getShippingAmount());
        totals.put("totalAmount", order.getTotalAmount());
        totals.put("success", true);
        
        return totals;
    }

    @Override
    public Map<String, Object> createOrder(String userId, Object items, String deliveryAddress, String paymentMethod) {
        // Convert items to Map format for the main createOrder method
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("items", items);
        orderData.put("deliveryAddress", deliveryAddress);
        orderData.put("paymentMethod", paymentMethod);
        
        return createOrder(userId, orderData);
    }

    @Override
    public Map<String, Object> updateOrderTracking(String orderId, String trackingNumber, Object estimatedDelivery) {
        Map<String, Object> trackingData = new HashMap<>();
        trackingData.put("trackingNumber", trackingNumber);
        trackingData.put("estimatedDelivery", estimatedDelivery);
        
        // Use a default userId for now - in real implementation, this should come from security context
        return updateOrderTracking(orderId, trackingData, "system");
    }

    @Override
    public Map<String, Object> updateOrderDelivery(String orderId, String trackingNumber, Object estimatedDelivery) {
        Map<String, Object> deliveryData = new HashMap<>();
        deliveryData.put("trackingNumber", trackingNumber);
        deliveryData.put("estimatedDelivery", estimatedDelivery);
        
        // Use a default userId for now - in real implementation, this should come from security context
        return updateOrderDelivery(orderId, "system", deliveryData);
    }

    private BigDecimal calculateTaxAmount(BigDecimal subtotal) {
        // TODO: Implement tax calculation logic based on location and tax rates
        return subtotal.multiply(BigDecimal.valueOf(0.08)); // 8% tax rate
    }

    private BigDecimal calculateShippingAmount(BigDecimal subtotal) {
        // TODO: Implement shipping calculation logic
        if (subtotal.compareTo(BigDecimal.valueOf(50)) >= 0) {
            return BigDecimal.ZERO; // Free shipping for orders over $50
        }
        return BigDecimal.valueOf(5.99); // Standard shipping
    }

    private BigDecimal calculateTotalAmount(BigDecimal subtotal, BigDecimal taxAmount, BigDecimal shippingAmount) {
        return subtotal.add(taxAmount).add(shippingAmount);
    }
}
