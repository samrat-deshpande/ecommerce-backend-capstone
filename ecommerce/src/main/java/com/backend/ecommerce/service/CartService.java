package com.backend.ecommerce.service;

import java.util.Map;

/**
 * Service interface for managing shopping cart operations
 */
public interface CartService {
    
    /**
     * Get user's active cart
     * @param userId User ID
     * @return Cart details with items
     */
    Map<String, Object> getUserCart(String userId);
    
    /**
     * Add product to cart
     * @param userId User ID
     * @param productId Product ID
     * @param quantity Quantity to add
     * @return Response with cart details
     */
    Map<String, Object> addToCart(String userId, String productId, Integer quantity);
    
    /**
     * Update item quantity in cart
     * @param userId User ID
     * @param itemId Cart item ID
     * @param quantity New quantity
     * @return Response with updated cart details
     */
    Map<String, Object> updateCartItemQuantity(String userId, String itemId, Integer quantity);
    
    /**
     * Remove item from cart
     * @param userId User ID
     * @param itemId Cart item ID
     * @return Response with updated cart details
     */
    Map<String, Object> removeFromCart(String userId, String itemId);
    
    /**
     * Clear user's cart
     * @param userId User ID
     * @return Response with confirmation
     */
    Map<String, Object> clearCart(String userId);
    
    /**
     * Get cart summary (item count, total amount)
     * @param userId User ID
     * @return Cart summary
     */
    Map<String, Object> getCartSummary(String userId);
    
    /**
     * Check if product exists in user's cart
     * @param userId User ID
     * @param productId Product ID
     * @return True if product exists in cart
     */
    boolean isProductInCart(String userId, String productId);
    
    /**
     * Get cart item count for user
     * @param userId User ID
     * @return Number of items in cart
     */
    Integer getCartItemCount(String userId);
    
    /**
     * Get cart total amount for user
     * @param userId User ID
     * @return Total cart amount
     */
    Double getCartTotalAmount(String userId);
    
    /**
     * Apply discount to cart
     * @param userId User ID
     * @param discountCode Discount code
     * @return Response with applied discount
     */
    Map<String, Object> applyDiscount(String userId, String discountCode);
    
    /**
     * Remove discount from cart
     * @param userId User ID
     * @return Response with removed discount
     */
    Map<String, Object> removeDiscount(String userId);
    
    /**
     * Validate cart items (check stock, prices)
     * @param userId User ID
     * @return Validation results
     */
    Map<String, Object> validateCart(String userId);
    
    /**
     * Convert cart to order
     * @param userId User ID
     * @param deliveryAddress Delivery address details
     * @return Order creation response
     */
    Map<String, Object> checkoutCart(String userId, Map<String, Object> deliveryAddress);
    
    /**
     * Save cart for later (if user is not logged in)
     * @param sessionId Session ID
     * @param cartData Cart data
     * @return Response with saved cart ID
     */
    Map<String, Object> saveCartForLater(String sessionId, Map<String, Object> cartData);
    
    /**
     * Restore saved cart to user account
     * @param userId User ID
     * @param savedCartId Saved cart ID
     * @return Response with restored cart
     */
    Map<String, Object> restoreSavedCart(String userId, String savedCartId);
    
    /**
     * Convert cart to order (overloaded method)
     * @param userId User ID
     * @return Order creation response
     */
    Map<String, Object> checkoutCart(String userId);
    
    /**
     * Save cart for later (overloaded method)
     * @param userId User ID
     * @return Response with saved cart ID
     */
    Map<String, Object> saveCartForLater(String userId);
}
