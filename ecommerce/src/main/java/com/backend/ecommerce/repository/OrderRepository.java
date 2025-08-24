package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    
    /**
     * Find orders by user ID with pagination
     */
    Page<Order> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find orders by user ID
     */
    List<Order> findByUserId(String userId);
    
    /**
     * Find orders by status
     */
    List<Order> findByStatus(Order.OrderStatus status);
    
    /**
     * Find orders by payment status
     */
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    /**
     * Find orders by payment method
     */
    List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod);
    
    /**
     * Find orders by tracking number
     */
    Optional<Order> findByTrackingNumber(String trackingNumber);
    
    /**
     * Find orders created after specified date
     */
    List<Order> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find orders created between specified dates
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find orders with total amount greater than specified value
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :minAmount")
    List<Order> findByTotalAmountGreaterThan(@Param("minAmount") Double minAmount);
    
    /**
     * Find orders with total amount between specified values
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount")
    List<Order> findByTotalAmountBetween(@Param("minAmount") Double minAmount, 
                                        @Param("maxAmount") Double maxAmount);
    
    /**
     * Find orders by delivery city
     */
    List<Order> findByDeliveryCity(String deliveryCity);
    
    /**
     * Find orders by delivery state
     */
    List<Order> findByDeliveryState(String deliveryState);
    
    /**
     * Find orders by delivery country
     */
    List<Order> findByDeliveryCountry(String deliveryCountry);
    
    /**
     * Find orders with estimated delivery date before specified date
     */
    @Query("SELECT o FROM Order o WHERE o.estimatedDeliveryDate < :date")
    List<Order> findOverdueOrders(@Param("date") LocalDateTime date);
    
    /**
     * Count orders by status
     */
    long countByStatus(Order.OrderStatus status);
    
    /**
     * Count orders by payment status
     */
    long countByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    /**
     * Count orders by user
     */
    long countByUserId(String userId);
    
    /**
     * Find orders that need shipping updates
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('CONFIRMED', 'PROCESSING') AND o.updatedAt < :threshold")
    List<Order> findOrdersNeedingShippingUpdate(@Param("threshold") LocalDateTime threshold);
    
    /**
     * Find high-value orders (above specified amount)
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :threshold ORDER BY o.totalAmount DESC")
    List<Order> findHighValueOrders(@Param("threshold") Double threshold);
}
