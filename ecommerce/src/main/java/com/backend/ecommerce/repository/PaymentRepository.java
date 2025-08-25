package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    
    /**
     * Find payment by transaction ID
     */
    Optional<Payment> findByTransactionId(String transactionId);
    
    /**
     * Find payments by order ID
     */
    List<Payment> findByOrderId(String orderId);
    
    /**
     * Find payments by user ID
     */
    List<Payment> findByUserId(String userId);
    
    /**
     * Find payments by user ID with pagination
     */
    org.springframework.data.domain.Page<Payment> findByUserId(String userId, org.springframework.data.domain.Pageable pageable);
    
    /**
     * Find payments by status
     */
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    /**
     * Find payments by payment method
     */
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    /**
     * Find payments by amount range
     */
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount")
    List<Payment> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                     @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Find payments with amount greater than specified value
     */
    @Query("SELECT p FROM Payment p WHERE p.amount > :amount")
    List<Payment> findByAmountGreaterThan(@Param("amount") BigDecimal amount);
    
    /**
     * Find payments created after specified date
     */
    List<Payment> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find payments created between specified dates
     */
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find payments processed after specified date
     */
    @Query("SELECT p FROM Payment p WHERE p.processedAt IS NOT NULL AND p.processedAt > :date")
    List<Payment> findByProcessedAtAfter(@Param("date") LocalDateTime date);
    
    /**
     * Find failed payments
     */
    List<Payment> findByStatusAndGatewayErrorMessageIsNotNull(Payment.PaymentStatus status);
    
    /**
     * Find payments by card brand
     */
    List<Payment> findByCardBrand(String cardBrand);
    
    /**
     * Find payments by card last four digits
     */
    List<Payment> findByCardLastFour(String cardLastFour);
    
    /**
     * Find payments by billing city
     */
    List<Payment> findByBillingCity(String billingCity);
    
    /**
     * Find payments by billing state
     */
    List<Payment> findByBillingState(String billingState);
    
    /**
     * Find payments by billing country
     */
    List<Payment> findByBillingCountry(String billingCountry);
    
    /**
     * Count payments by status
     */
    long countByStatus(Payment.PaymentStatus status);
    
    /**
     * Count payments by payment method
     */
    long countByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    /**
     * Count payments by user
     */
    long countByUserId(String userId);
    
    /**
     * Sum total amount of successful payments
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'PROCESSED'")
    BigDecimal sumSuccessfulPayments();
    
    /**
     * Sum total amount of payments by user
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.userId = :userId AND p.status = 'PROCESSED'")
    BigDecimal sumSuccessfulPaymentsByUser(@Param("userId") String userId);
    
    /**
     * Find payments with specific gateway error codes
     */
    @Query("SELECT p FROM Payment p WHERE p.gatewayErrorCode IN :errorCodes")
    List<Payment> findByGatewayErrorCodes(@Param("errorCodes") List<String> errorCodes);
    
    /**
     * Find payments that need retry (failed payments older than threshold)
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' AND p.createdAt < :threshold")
    List<Payment> findPaymentsNeedingRetry(@Param("threshold") LocalDateTime threshold);
    
    /**
     * Find high-value payments (above specified amount)
     */
    @Query("SELECT p FROM Payment p WHERE p.amount > :threshold ORDER BY p.amount DESC")
    List<Payment> findHighValuePayments(@Param("threshold") BigDecimal threshold);
}
