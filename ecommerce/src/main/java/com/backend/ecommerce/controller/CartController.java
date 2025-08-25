package com.backend.ecommerce.controller;

import com.backend.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing shopping cart operations
 * Provides endpoints for cart management, item operations, and checkout
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@Tag(name = "Shopping Cart", description = "APIs for managing shopping cart, adding/removing items, and checkout process")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Get user's cart
     */
    @GetMapping
    @Operation(
        summary = "Get user's cart",
        description = "Retrieves the current shopping cart for the authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"cart\": {\"id\": \"uuid\", \"items\": [...], \"totalAmount\": 99.99}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart not found"
        )
    })
    public ResponseEntity<Map<String, Object>> getUserCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId) {
        
        Map<String, Object> response = cartService.getUserCart(userId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add product to cart
     */
    @PostMapping("/add")
    @Operation(
        summary = "Add product to cart",
        description = "Adds a product to the user's shopping cart with specified quantity"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product added to cart successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Product added to cart\", \"cart\": {...}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input or insufficient stock",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": false, \"error\": \"Insufficient stock\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> addToCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Product ID", example = "product-uuid")
            @RequestParam String productId,
            @Parameter(description = "Quantity to add", example = "2")
            @RequestParam Integer quantity) {
        
        Map<String, Object> response = cartService.addToCart(userId, productId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/items/{itemId}")
    @Operation(
        summary = "Update cart item quantity",
        description = "Updates the quantity of a specific item in the shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Quantity updated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Quantity updated\", \"cart\": {...}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid quantity or insufficient stock"
        )
    })
    public ResponseEntity<Map<String, Object>> updateCartItemQuantity(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Cart item ID", example = "item-uuid")
            @PathVariable String itemId,
            @Parameter(description = "New quantity", example = "3")
            @RequestParam Integer quantity) {
        
        Map<String, Object> response = cartService.updateCartItemQuantity(userId, itemId, quantity);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{itemId}")
    @Operation(
        summary = "Remove item from cart",
        description = "Removes a specific item from the shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item removed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Item removed\", \"cart\": {...}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cart item not found"
        )
    })
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Cart item ID", example = "item-uuid")
            @PathVariable String itemId) {
        
        Map<String, Object> response = cartService.removeFromCart(userId, itemId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Clear user's cart
     */
    @DeleteMapping("/clear")
    @Operation(
        summary = "Clear user's cart",
        description = "Removes all items from the user's shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart cleared successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Cart cleared\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> clearCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId) {
        
        Map<String, Object> response = cartService.clearCart(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get cart summary
     */
    @GetMapping("/summary")
    @Operation(
        summary = "Get cart summary",
        description = "Retrieves a summary of the user's cart including item count and total amount"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart summary retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"itemCount\": 3, \"totalAmount\": 149.97}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getCartSummary(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId) {
        
        Map<String, Object> response = cartService.getCartSummary(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Check if product exists in cart
     */
    @GetMapping("/check/{productId}")
    @Operation(
        summary = "Check if product exists in cart",
        description = "Checks whether a specific product is already in the user's cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"exists\": true, \"quantity\": 2}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> checkProductInCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Product ID to check", example = "product-uuid")
            @PathVariable String productId) {
        
        boolean exists = cartService.isProductInCart(userId, productId);
        Integer quantity = cartService.getCartItemCount(userId);
        
        Map<String, Object> response = Map.of(
            "success", true,
            "exists", exists,
            "quantity", exists ? quantity : 0
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Apply discount to cart
     */
    @PostMapping("/discount")
    @Operation(
        summary = "Apply discount to cart",
        description = "Applies a discount code to the user's shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Discount applied successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Discount applied\", \"discountAmount\": 10.00}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or expired discount code"
        )
    })
    public ResponseEntity<Map<String, Object>> applyDiscount(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Discount code", example = "SAVE20")
            @RequestParam String discountCode) {
        
        Map<String, Object> response = cartService.applyDiscount(userId, discountCode);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Remove discount from cart
     */
    @DeleteMapping("/discount")
    @Operation(
        summary = "Remove discount from cart",
        description = "Removes any applied discount from the user's shopping cart"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Discount removed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Discount removed\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> removeDiscount(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId) {
        
        Map<String, Object> response = cartService.removeDiscount(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Validate cart items
     */
    @PostMapping("/validate")
    @Operation(
        summary = "Validate cart items",
        description = "Validates all items in the cart for stock availability and price accuracy"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart validation completed",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"valid\": true, \"issues\": []}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> validateCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId) {
        
        Map<String, Object> response = cartService.validateCart(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Checkout cart
     */
    @PostMapping("/checkout")
    @Operation(
        summary = "Checkout cart",
        description = "Converts the shopping cart to an order and processes checkout"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Checkout completed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Order created\", \"orderId\": \"order-uuid\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Checkout failed - validation errors or insufficient stock"
        )
    })
    public ResponseEntity<Map<String, Object>> checkoutCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @RequestBody(required = false) Map<String, Object> deliveryAddress) {
        
        Map<String, Object> response;
        if (deliveryAddress != null) {
            response = cartService.checkoutCart(userId, deliveryAddress);
        } else {
            response = cartService.checkoutCart(userId);
        }
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Save cart for later
     */
    @PostMapping("/save")
    @Operation(
        summary = "Save cart for later",
        description = "Saves the current cart state for later retrieval (useful for guest users)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart saved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Cart saved\", \"savedCartId\": \"saved-uuid\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> saveCartForLater(
            @Parameter(description = "Session ID or user ID", example = "session-uuid")
            @RequestParam String sessionId,
            @RequestBody Map<String, Object> cartData) {
        
        Map<String, Object> response = cartService.saveCartForLater(sessionId, cartData);
        return ResponseEntity.ok(response);
    }

    /**
     * Restore saved cart
     */
    @PostMapping("/restore/{savedCartId}")
    @Operation(
        summary = "Restore saved cart",
        description = "Restores a previously saved cart to the user's account"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cart restored successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"success\": true, \"message\": \"Cart restored\", \"cart\": {...}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Saved cart not found"
        )
    })
    public ResponseEntity<Map<String, Object>> restoreSavedCart(
            @Parameter(description = "User ID", example = "user-uuid")
            @RequestParam String userId,
            @Parameter(description = "Saved cart ID", example = "saved-uuid")
            @PathVariable String savedCartId) {
        
        Map<String, Object> response = cartService.restoreSavedCart(userId, savedCartId);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
