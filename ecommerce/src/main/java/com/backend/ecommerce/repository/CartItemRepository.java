package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity
 * Provides data access methods for shopping cart items
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    
    /**
     * Find all cart items for a specific cart
     * @param cartId the ID of the cart
     * @return list of cart items
     */
    List<CartItem> findByCartId(String cartId);
    
    /**
     * Find cart item by cart ID and product ID
     * @param cartId the ID of the cart
     * @param productId the ID of the product
     * @return optional cart item
     */
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
    
    /**
     * Find all cart items for a specific product across all carts
     * @param productId the ID of the product
     * @return list of cart items
     */
    List<CartItem> findByProductId(String productId);
    
    /**
     * Count cart items in a specific cart
     * @param cartId the ID of the cart
     * @return number of cart items
     */
    long countByCartId(String cartId);
    
    /**
     * Delete all cart items for a specific cart
     * @param cartId the ID of the cart
     */
    void deleteByCartId(String cartId);
    
    /**
     * Delete cart item by cart ID and product ID
     * @param cartId the ID of the cart
     * @param productId the ID of the product
     */
    void deleteByCartIdAndProductId(String cartId, String productId);
    
    /**
     * Find cart items with quantity greater than specified value
     * @param cartId the ID of the cart
     * @param quantity the minimum quantity
     * @return list of cart items
     */
    List<CartItem> findByCartIdAndQuantityGreaterThan(String cartId, Integer quantity);
    
    /**
     * Find cart items by cart ID ordered by creation date
     * @param cartId the ID of the cart
     * @return list of cart items ordered by creation date
     */
    List<CartItem> findByCartIdOrderByCreatedAtAsc(String cartId);
    
    /**
     * Check if a product exists in a specific cart
     * @param cartId the ID of the cart
     * @param productId the ID of the product
     * @return true if product exists in cart, false otherwise
     */
    boolean existsByCartIdAndProductId(String cartId, String productId);
    
    /**
     * Find cart items with subtotal greater than specified value
     * @param cartId the ID of the cart
     * @param subtotal the minimum subtotal
     * @return list of cart items
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.subtotal > :subtotal")
    List<CartItem> findByCartIdAndSubtotalGreaterThan(@Param("cartId") String cartId, @Param("subtotal") java.math.BigDecimal subtotal);
    
    /**
     * Get total value of all items in a cart
     * @param cartId the ID of the cart
     * @return total value as BigDecimal
     */
    @Query("SELECT SUM(ci.subtotal) FROM CartItem ci WHERE ci.cart.id = :cartId")
    java.math.BigDecimal getCartTotalValue(@Param("cartId") String cartId);
    
    /**
     * Find cart items created within a date range
     * @param cartId the ID of the cart
     * @param startDate start date
     * @param endDate end date
     * @return list of cart items
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.createdAt BETWEEN :startDate AND :endDate")
    List<CartItem> findByCartIdAndCreatedAtBetween(@Param("cartId") String cartId, 
                                                   @Param("startDate") java.time.LocalDateTime startDate, 
                                                   @Param("endDate") java.time.LocalDateTime endDate);
}
