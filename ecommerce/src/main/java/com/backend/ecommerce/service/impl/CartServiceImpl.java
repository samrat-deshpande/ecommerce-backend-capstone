package com.backend.ecommerce.service.impl;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getUserCart(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", cart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> addToCart(String userId, String productId, Integer quantity) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate product exists and has sufficient stock
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Get or create cart
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Insufficient stock available for requested quantity");
            }
            item.setQuantity(newQuantity);
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new cart item
            CartItem newItem = new CartItem();
            newItem.setId(UUID.randomUUID().toString());
            newItem.setCart(cart);
            newItem.setProductId(productId);
            newItem.setProductName(product.getName());
            newItem.setProductImage(product.getImageUrl());
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            newItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setUpdatedAt(LocalDateTime.now());

            cart.getItems().add(newItem);
        }

        // Recalculate cart totals
        cart.recalculateTotals();
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> updateCartItemQuantity(String userId, String itemId, Integer quantity) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            // Remove item if quantity is 0 or negative
            cart.getItems().remove(item);
        } else {
            // Validate stock availability
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock available");
            }

            item.setQuantity(quantity);
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            item.setUpdatedAt(LocalDateTime.now());
        }

        cart.recalculateTotals();
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> removeFromCart(String userId, String itemId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        
        cart.recalculateTotals();
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> clearCart(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.clearItems();
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> getCartSummary(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("itemCount", cart.getItemCount());
        summary.put("totalAmount", cart.getTotalAmount());
        summary.put("success", true);
        
        return summary;
    }

    @Override
    public boolean isProductInCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElse(null);
        
        if (cart == null) return false;
        
        return cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(productId));
    }

    @Override
    public Integer getCartItemCount(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));
        return cart.getItemCount();
    }

    @Override
    public Double getCartTotalAmount(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(userId));
        return cart.getTotalAmount().doubleValue();
    }

    @Override
    public Map<String, Object> applyDiscount(String userId, String discountCode) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        // TODO: Implement discount logic
        // This would typically involve:
        // 1. Validating the discount code
        // 2. Checking if it's applicable to the cart
        // 3. Applying the discount calculation
        // 4. Updating cart totals
        
        cart.setUpdatedAt(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> removeDiscount(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        // TODO: Remove any applied discounts and recalculate totals
        
        cart.setUpdatedAt(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> validateCart(String userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElse(null);
        
        Map<String, Object> validation = new HashMap<>();
        
        if (cart == null || cart.getItems().isEmpty()) {
            validation.put("valid", false);
            validation.put("message", "Cart is empty");
            return validation;
        }

        // Validate each item
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null || !product.isActive()) {
                validation.put("valid", false);
                validation.put("message", "Product no longer exists or is inactive");
                return validation;
            }
            if (product.getStockQuantity() < item.getQuantity()) {
                validation.put("valid", false);
                validation.put("message", "Insufficient stock for " + product.getName());
                return validation;
            }
        }

        validation.put("valid", true);
        validation.put("message", "Cart is valid");
        return validation;
    }

    @Override
    public Map<String, Object> checkoutCart(String userId, Map<String, Object> deliveryAddress) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        Map<String, Object> validation = validateCart(userId);
        if (!(Boolean) validation.get("valid")) {
            throw new RuntimeException("Cart validation failed: " + validation.get("message"));
        }

        // Mark cart as converted
        cart.setStatus(Cart.CartStatus.CONVERTED);
        cart.setUpdatedAt(LocalDateTime.now());

        Cart savedCart = cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", savedCart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> saveCartForLater(String sessionId, Map<String, Object> cartData) {
        // TODO: Implement save for later logic
        // This could involve creating a separate saved cart or marking items as saved
        
        Map<String, Object> response = new HashMap<>();
        response.put("savedCartId", UUID.randomUUID().toString());
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> restoreSavedCart(String userId, String savedCartId) {
        // TODO: Implement restore saved cart logic
        // This would typically involve:
        // 1. Finding the saved cart
        // 2. Creating a new active cart with the saved items
        // 3. Validating current stock availability
        
        Cart cart = createNewCart(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cart", cart);
        response.put("success", true);
        return response;
    }

    @Override
    public Map<String, Object> checkoutCart(String userId) {
        return checkoutCart(userId, new HashMap<>());
    }

    @Override
    public Map<String, Object> saveCartForLater(String userId) {
        return saveCartForLater(userId, new HashMap<>());
    }

    private Cart createNewCart(String userId) {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUserId(userId);
        cart.setStatus(Cart.CartStatus.ACTIVE);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }
}
