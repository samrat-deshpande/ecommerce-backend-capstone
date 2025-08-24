package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    
    /**
     * Find active cart by user ID
     */
    Optional<Cart> findByUserIdAndStatus(String userId, Cart.CartStatus status);
    
    /**
     * Find all carts by user ID
     */
    List<Cart> findByUserId(String userId);
    
    /**
     * Find carts by status
     */
    List<Cart> findByStatus(Cart.CartStatus status);
    
    /**
     * Find expired carts (older than specified days)
     */
    @Query("SELECT c FROM Cart c WHERE c.status = 'ACTIVE' AND c.updatedAt < :expiryDate")
    List<Cart> findExpiredCarts(@Param("expiryDate") LocalDateTime expiryDate);
    
    /**
     * Find carts with items count greater than specified value
     */
    @Query("SELECT c FROM Cart c WHERE c.itemCount > :minItemCount")
    List<Cart> findByItemCountGreaterThan(@Param("minItemCount") Integer minItemCount);
    
    /**
     * Find carts with total amount greater than specified value
     */
    @Query("SELECT c FROM Cart c WHERE c.totalAmount > :minAmount")
    List<Cart> findByTotalAmountGreaterThan(@Param("minAmount") Double minAmount);
    
    /**
     * Find carts created after specified date
     */
    List<Cart> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find carts updated after specified date
     */
    List<Cart> findByUpdatedAtAfter(LocalDateTime date);
    
    /**
     * Check if user has active cart
     */
    boolean existsByUserIdAndStatus(String userId, Cart.CartStatus status);
    
    /**
     * Count active carts by user
     */
    long countByUserIdAndStatus(String userId, Cart.CartStatus status);
}
