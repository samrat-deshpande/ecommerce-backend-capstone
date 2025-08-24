package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing shopping cart operations
 * Provides endpoints for cart management, add/remove items, and checkout
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Get user's cart
     * @param userId User ID (from authentication)
     * @return Cart details with items
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserCart(@RequestParam String userId) {
        Map<String, Object> response = cartService.getUserCart(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Add product to cart
     * @param userId User ID (from authentication)
     * @param request Request body with productId and quantity
     * @return Response with updated cart
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam String userId,
            @RequestBody Map<String, Object> request) {
        
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        
        if (productId == null || quantity == null || quantity <= 0) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Invalid product ID or quantity"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Map<String, Object> response = cartService.addToCart(userId, productId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update cart item quantity
     * @param userId User ID (from authentication)
     * @param itemId Cart item ID
     * @param request Request body with new quantity
     * @return Response with updated cart
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> updateCartItemQuantity(
            @RequestParam String userId,
            @PathVariable String itemId,
            @RequestBody Map<String, Object> request) {
        
        Integer quantity = (Integer) request.get("quantity");
        
        if (quantity == null || quantity <= 0) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Invalid quantity"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Map<String, Object> response = cartService.updateCartItemQuantity(userId, itemId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Remove item from cart
     * @param userId User ID (from authentication)
     * @param itemId Cart item ID
     * @return Response with updated cart
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam String userId,
            @PathVariable String itemId) {
        
        Map<String, Object> response = cartService.removeFromCart(userId, itemId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Clear user's cart
     * @param userId User ID (from authentication)
     * @return Response with confirmation
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart(@RequestParam String userId) {
        Map<String, Object> response = cartService.clearCart(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get cart summary (item count, total amount)
     * @param userId User ID (from authentication)
     * @return Cart summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getCartSummary(@RequestParam String userId) {
        Map<String, Object> response = cartService.getCartSummary(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Check if product exists in user's cart
     * @param userId User ID (from authentication)
     * @param productId Product ID
     * @return Response indicating if product is in cart
     */
    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Object>> checkProductInCart(
            @RequestParam String userId,
            @PathVariable String productId) {
        
        boolean isInCart = cartService.isProductInCart(userId, productId);
        
        Map<String, Object> response = Map.of(
            "success", true,
            "isInCart", isInCart,
            "productId", productId
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Apply discount to cart
     * @param userId User ID (from authentication)
     * @param request Request body with discount code
     * @return Response with applied discount
     */
    @PostMapping("/discount")
    public ResponseEntity<Map<String, Object>> applyDiscount(
            @RequestParam String userId,
            @RequestBody Map<String, Object> request) {
        
        String discountCode = (String) request.get("discountCode");
        
        if (discountCode == null || discountCode.trim().isEmpty()) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Discount code is required"
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Map<String, Object> response = cartService.applyDiscount(userId, discountCode);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Remove discount from cart
     * @param userId User ID (from authentication)
     * @return Response with removed discount
     */
    @DeleteMapping("/discount")
    public ResponseEntity<Map<String, Object>> removeDiscount(@RequestParam String userId) {
        Map<String, Object> response = cartService.removeDiscount(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Validate cart items (check stock, prices)
     * @param userId User ID (from authentication)
     * @return Validation results
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCart(@RequestParam String userId) {
        Map<String, Object> response = cartService.validateCart(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Checkout cart (convert to order)
     * @param userId User ID (from authentication)
     * @param deliveryAddress Delivery address details
     * @return Order creation response
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkoutCart(
            @RequestParam String userId,
            @RequestBody Map<String, Object> deliveryAddress) {
        
        Map<String, Object> response = cartService.checkoutCart(userId, deliveryAddress);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Save cart for later (for guest users)
     * @param sessionId Session ID
     * @param cartData Cart data
     * @return Response with saved cart ID
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveCartForLater(
            @RequestParam String sessionId,
            @RequestBody Map<String, Object> cartData) {
        
        Map<String, Object> response = cartService.saveCartForLater(sessionId, cartData);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Restore saved cart to user account
     * @param userId User ID (from authentication)
     * @param savedCartId Saved cart ID
     * @return Response with restored cart
     */
    @PostMapping("/restore/{savedCartId}")
    public ResponseEntity<Map<String, Object>> restoreSavedCart(
            @RequestParam String userId,
            @PathVariable String savedCartId) {
        
        Map<String, Object> response = cartService.restoreSavedCart(userId, savedCartId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
