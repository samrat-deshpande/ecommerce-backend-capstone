package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for OrderItem entity
 * Provides data access methods for order line items
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    
    /**
     * Find all order items for a specific order
     * @param orderId the ID of the order
     * @return list of order items
     */
    List<OrderItem> findByOrderId(String orderId);
    
    /**
     * Find order item by order ID and product ID
     * @param orderId the ID of the order
     * @param productId the ID of the product
     * @return optional order item
     */
    Optional<OrderItem> findByOrderIdAndProductId(String orderId, String productId);
    
    /**
     * Find all order items for a specific product across all orders
     * @param productId the ID of the product
     * @return list of order items
     */
    List<OrderItem> findByProductId(String productId);
    
    /**
     * Count order items in a specific order
     * @param orderId the ID of the order
     * @return number of order items
     */
    long countByOrderId(String orderId);
    
    /**
     * Find order items with quantity greater than specified value
     * @param orderId the ID of the order
     * @param quantity the minimum quantity
     * @return list of order items
     */
    List<OrderItem> findByOrderIdAndQuantityGreaterThan(String orderId, Integer quantity);
    
    /**
     * Find order items by order ID ordered by creation date
     * @param orderId the ID of the order
     * @return list of order items ordered by creation date
     */
    List<OrderItem> findByOrderIdOrderByCreatedAtAsc(String orderId);
    
    /**
     * Check if a product exists in a specific order
     * @param orderId the ID of the order
     * @param productId the ID of the product
     * @return true if product exists in order, false otherwise
     */
    boolean existsByOrderIdAndProductId(String orderId, String productId);
    
    /**
     * Find order items with subtotal greater than specified value
     * @param orderId the ID of the order
     * @param subtotal the minimum subtotal
     * @return list of order items
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.subtotal > :subtotal")
    List<OrderItem> findByOrderIdAndSubtotalGreaterThan(@Param("orderId") String orderId, @Param("subtotal") java.math.BigDecimal subtotal);
    
    /**
     * Get total value of all items in an order
     * @param orderId the ID of the order
     * @return total value as BigDecimal
     */
    @Query("SELECT SUM(oi.subtotal) FROM OrderItem oi WHERE oi.order.id = :orderId")
    java.math.BigDecimal getOrderTotalValue(@Param("orderId") String orderId);
    
    /**
     * Find order items created within a date range
     * @param orderId the ID of the order
     * @param startDate start date
     * @param endDate end date
     * @return list of order items
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.createdAt BETWEEN :startDate AND :endDate")
    List<OrderItem> findByOrderIdAndCreatedAtBetween(@Param("orderId") String orderId, 
                                                     @Param("startDate") java.time.LocalDateTime startDate, 
                                                     @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find order items by product name (case-insensitive)
     * @param productName the name of the product
     * @return list of order items
     */
    List<OrderItem> findByProductNameContainingIgnoreCase(String productName);
    
    /**
     * Find order items with unit price in a specific range
     * @param minPrice minimum unit price
     * @param maxPrice maximum unit price
     * @return list of order items
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<OrderItem> findByUnitPriceBetween(@Param("minPrice") java.math.BigDecimal minPrice, 
                                          @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    /**
     * Get average unit price across all order items
     * @return average unit price as BigDecimal
     */
    @Query("SELECT AVG(oi.unitPrice) FROM OrderItem oi")
    java.math.BigDecimal getAverageUnitPrice();
    
    /**
     * Find order items with highest subtotals
     * @param limit maximum number of results
     * @return list of order items ordered by subtotal descending
     */
    @Query("SELECT oi FROM OrderItem oi ORDER BY oi.subtotal DESC")
    List<OrderItem> findTopOrderItemsBySubtotal(@Param("limit") int limit);
}
