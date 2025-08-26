package com.backend.ecommerce.config;

import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Data Loader Configuration
 * This class provides sample data for development and testing purposes.
 * It only runs when the 'dev' or 'test' profile is active.
 */
@Configuration
@Profile({"dev", "test"})
public class DataLoader {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * CommandLineRunner bean that executes after the application context is loaded
     * Only runs when the application starts with dev or test profile
     */
    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Only load data if no users exist
            if (userRepository.count() == 0) {
                loadSampleData();
            }
        };
    }

    /**
     * Loads comprehensive sample data for the ecommerce application
     */
    private void loadSampleData() {
        System.out.println("Loading sample data...");
        
        // Load users first
        List<User> users = loadUsers();
        
        // Load categories
        List<Category> categories = loadCategories();
        
        // Load products
        List<Product> products = loadProducts(categories);
        
        // Load carts and cart items
        loadCartsAndItems(users, products);
        
        // Load orders, order items, and payments
        loadOrdersAndPayments(users, products);
        
        System.out.println("Sample data loaded successfully!");
    }

    /**
     * Loads sample users with different roles
     */
    private List<User> loadUsers() {
        List<User> users = Arrays.asList(
            createUser("john.doe@example.com", "John", "Doe", "+1-555-0101", User.UserRole.USER, true, true),
            createUser("jane.smith@example.com", "Jane", "Smith", "+1-555-0102", User.UserRole.USER, true, false),
            createUser("mike.johnson@example.com", "Mike", "Johnson", "+1-555-0103", User.UserRole.USER, true, true),
            createUser("sarah.wilson@example.com", "Sarah", "Wilson", "+1-555-0104", User.UserRole.USER, false, false),
            createUser("admin@ecommerce.com", "Admin", "User", "+1-555-0000", User.UserRole.ADMIN, true, true),
            createUser("moderator@ecommerce.com", "Moderator", "User", "+1-555-0001", User.UserRole.MODERATOR, true, true)
        );
        
        return userRepository.saveAll(users);
    }

    /**
     * Creates a user with the specified details
     */
    private User createUser(String email, String firstName, String lastName, String phone, User.UserRole role, boolean emailVerified, boolean phoneVerified) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123")); // Same password for all users in dev
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phone);
        user.setRole(role);
        user.setEmailVerified(emailVerified);
        user.setPhoneVerified(phoneVerified);
        user.setActive(true);
        return user;
    }

    /**
     * Loads sample categories with hierarchical structure
     */
    private List<Category> loadCategories() {
        // Create root categories first
        Category electronics = createCategory("Electronics", "Latest electronic gadgets and devices", null, true);
        Category clothing = createCategory("Clothing", "Fashion and apparel for all ages", null, true);
        Category homeGarden = createCategory("Home & Garden", "Home improvement and garden supplies", null, false);
        Category books = createCategory("Books", "Books, magazines, and educational materials", null, false);
        
        List<Category> rootCategories = categoryRepository.saveAll(Arrays.asList(electronics, clothing, homeGarden, books));
        
        // Create subcategories
        Category smartphones = createCategory("Smartphones", "Mobile phones and smartphones", electronics.getId(), true);
        Category laptops = createCategory("Laptops", "Portable computers and laptops", electronics.getId(), true);
        Category mensClothing = createCategory("Men's Clothing", "Men's fashion and apparel", clothing.getId(), false);
        Category womensClothing = createCategory("Women's Clothing", "Women's fashion and apparel", clothing.getId(), false);
        
        List<Category> subCategories = categoryRepository.saveAll(Arrays.asList(smartphones, laptops, mensClothing, womensClothing));
        
        // Combine all categories
        List<Category> allCategories = new java.util.ArrayList<>();
        allCategories.addAll(rootCategories);
        allCategories.addAll(subCategories);
        
        return allCategories;
    }

    /**
     * Creates a category with the specified details
     */
    private Category createCategory(String name, String description, String parentId, boolean featured) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setParentId(parentId);
        category.setFeatured(featured);
        category.setActive(true);
        category.setSortOrder(0);
        category.setProductCount(0);
        return category;
    }

    /**
     * Loads sample products across different categories
     */
    private List<Product> loadProducts(List<Category> categories) {
        List<Product> products = Arrays.asList(
            // Electronics - Smartphones
            createProduct("iPhone 15 Pro", "Latest iPhone with advanced camera system and A17 Pro chip", 
                         new BigDecimal("999.99"), new BigDecimal("1099.99"), 9.09, 
                         "Electronics", "Smartphones", "Apple", "iPhone 15 Pro", 
                         "IPH15PRO-128", 50, 10, 0.187, "146.7 x 71.5 x 8.25 mm", true),
            
            createProduct("Samsung Galaxy S24", "Flagship Android smartphone with AI features", 
                         new BigDecimal("899.99"), new BigDecimal("899.99"), null, 
                         "Electronics", "Smartphones", "Samsung", "Galaxy S24", 
                         "SAMS24-256", 45, 10, 0.168, "147.0 x 70.6 x 7.6 mm", true),
            
            // Electronics - Laptops
            createProduct("MacBook Pro 14\"", "Professional laptop with M3 Pro chip", 
                         new BigDecimal("1999.99"), new BigDecimal("2199.99"), 9.09, 
                         "Electronics", "Laptops", "Apple", "MacBook Pro 14\"", 
                         "MBP14-M3PRO-512", 25, 5, 1.6, "312.6 x 221.2 x 15.5 mm", true),
            
            createProduct("Dell XPS 13", "Ultrabook with Intel Core i7 processor", 
                         new BigDecimal("1299.99"), new BigDecimal("1299.99"), null, 
                         "Electronics", "Laptops", "Dell", "XPS 13", 
                         "DELLXPS13-I7-512", 30, 5, 1.2, "302 x 199 x 14.8 mm", false),
            
            // Clothing
            createProduct("Men's Casual T-Shirt", "Comfortable cotton t-shirt for everyday wear", 
                         new BigDecimal("24.99"), new BigDecimal("29.99"), 16.67, 
                         "Clothing", "Men's Clothing", "Fashion Brand", "Casual T-Shirt", 
                         "MENS-TSHIRT-CASUAL", 100, 20, 0.2, "M, L, XL", false),
            
            createProduct("Women's Summer Dress", "Elegant summer dress with floral pattern", 
                         new BigDecimal("59.99"), new BigDecimal("79.99"), 25.00, 
                         "Clothing", "Women's Clothing", "Elegance", "Summer Dress", 
                         "WOMENS-DRESS-SUMMER", 75, 15, 0.3, "XS, S, M, L", true),
            
            // Home & Garden
            createProduct("Garden Tool Set", "Complete set of essential garden tools", 
                         new BigDecimal("89.99"), new BigDecimal("89.99"), null, 
                         "Home & Garden", null, "Garden Pro", "Tool Set", 
                         "GARDEN-TOOLSET-COMPLETE", 40, 8, 2.5, "Various sizes", false),
            
            // Books
            createProduct("The Great Gatsby", "Classic novel by F. Scott Fitzgerald", 
                         new BigDecimal("12.99"), new BigDecimal("12.99"), null, 
                         "Books", null, "Scribner", "Paperback", 
                         "BOOK-GATSBY-PAPERBACK", 200, 50, 0.3, "5.2 x 0.8 x 8 inches", false),
            
            // Additional Electronics
            createProduct("Wireless Bluetooth Headphones", "Noise-cancelling wireless headphones", 
                         new BigDecimal("149.99"), new BigDecimal("199.99"), 25.00, 
                         "Electronics", "Audio", "SoundMax", "Bluetooth Pro", 
                         "AUDIO-HEADPHONES-BT", 60, 12, 0.25, "180 x 180 x 80 mm", true),
            
            createProduct("Smart Watch Series 8", "Advanced fitness and health tracking smartwatch", 
                         new BigDecimal("399.99"), new BigDecimal("449.99"), 11.11, 
                         "Electronics", "Wearables", "TechWear", "Smart Watch 8", 
                         "WEARABLE-SMARTWATCH-8", 35, 7, 0.038, "41 x 35 x 10.7 mm", true)
        );
        
        return productRepository.saveAll(products);
    }

    /**
     * Creates a product with the specified details
     */
    private Product createProduct(String name, String description, BigDecimal price, BigDecimal originalPrice, 
                                Double discountPercentage, String category, String subCategory, String brand, 
                                String model, String sku, Integer stockQuantity, Integer minStockLevel, 
                                Double weight, String dimensions, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setOriginalPrice(originalPrice);
        product.setDiscountPercentage(discountPercentage);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setModel(model);
        product.setSku(sku);
        product.setStockQuantity(stockQuantity);
        product.setMinStockLevel(minStockLevel);
        product.setWeight(weight);
        product.setDimensions(dimensions);
        product.setFeatured(featured);
        product.setActive(true);
        product.setAverageRating(4.0 + Math.random() * 1.0); // Random rating between 4.0-5.0
        product.setReviewCount((int) (Math.random() * 200) + 50); // Random review count 50-250
        return product;
    }

    /**
     * Loads sample carts and cart items
     */
    private void loadCartsAndItems(List<User> users, List<Product> products) {
        // Create carts for first 3 users
        Cart cart1 = createCart(users.get(0).getId());
        Cart cart2 = createCart(users.get(1).getId());
        Cart cart3 = createCart(users.get(2).getId());
        
        List<Cart> carts = cartRepository.saveAll(Arrays.asList(cart1, cart2, cart3));
        
        // Create cart items
        List<CartItem> cartItems = Arrays.asList(
            createCartItem(carts.get(0), products.get(0), 1), // iPhone in cart 1
            createCartItem(carts.get(0), products.get(4), 2), // T-Shirts in cart 1
            createCartItem(carts.get(1), products.get(2), 1), // MacBook in cart 2
            createCartItem(carts.get(2), products.get(5), 1)  // Dress in cart 3
        );
        
        cartItemRepository.saveAll(cartItems);
        
        // Update cart totals
        updateCartTotals(carts);
    }

    /**
     * Creates a cart for the specified user
     */
    private Cart createCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStatus(Cart.CartStatus.ACTIVE);
        return cart;
    }

    /**
     * Creates a cart item with the specified details
     */
    private CartItem createCartItem(Cart cart, Product product, Integer quantity) {
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage("/images/products/" + product.getSku().toLowerCase() + ".jpg");
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.calculateSubtotal();
        return item;
    }

    /**
     * Updates cart totals based on cart items
     */
    private void updateCartTotals(List<Cart> carts) {
        for (Cart cart : carts) {
            List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
            BigDecimal total = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalAmount(total);
            cart.setItemCount(items.size());
            cartRepository.save(cart);
        }
    }

    /**
     * Loads sample orders, order items, and payments
     */
    private void loadOrdersAndPayments(List<User> users, List<Product> products) {
        // Create orders
        Order order1 = createOrder(users.get(0), "ORD-2024-001", Order.OrderStatus.CONFIRMED, Order.PaymentStatus.PAID);
        Order order2 = createOrder(users.get(1), "ORD-2024-002", Order.OrderStatus.SHIPPED, Order.PaymentStatus.PAID);
        Order order3 = createOrder(users.get(2), "ORD-2024-003", Order.OrderStatus.DELIVERED, Order.PaymentStatus.PAID);
        
        List<Order> orders = orderRepository.saveAll(Arrays.asList(order1, order2, order3));
        
        // Create order items
        List<OrderItem> orderItems = Arrays.asList(
            createOrderItem(orders.get(0), products.get(0), 1), // iPhone in order 1
            createOrderItem(orders.get(0), products.get(4), 2), // T-Shirts in order 1
            createOrderItem(orders.get(1), products.get(2), 1), // MacBook in order 2
            createOrderItem(orders.get(2), products.get(5), 1)  // Dress in order 3
        );
        
        orderItemRepository.saveAll(orderItems);
        
        // Update order totals
        updateOrderTotals(orders);
        
        // Create payments
        List<Payment> payments = Arrays.asList(
            createPayment(orders.get(0), users.get(0), Payment.PaymentStatus.SUCCESSFUL, Payment.PaymentMethod.CREDIT_CARD),
            createPayment(orders.get(1), users.get(1), Payment.PaymentStatus.SUCCESSFUL, Payment.PaymentMethod.CREDIT_CARD),
            createPayment(orders.get(2), users.get(2), Payment.PaymentStatus.SUCCESSFUL, Payment.PaymentMethod.DEBIT_CARD)
        );
        
        paymentRepository.saveAll(payments);
    }

    /**
     * Creates an order with the specified details
     */
    private Order createOrder(User user, String orderNumber, Order.OrderStatus status, Order.PaymentStatus paymentStatus) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(user.getId());
        order.setStatus(status);
        order.setPaymentStatus(paymentStatus);
        order.setPaymentMethod(Order.PaymentMethod.CREDIT_CARD);
        order.setDeliveryAddress("123 Main Street");
        order.setDeliveryCity("New York");
        order.setDeliveryState("NY");
        order.setDeliveryZipCode("10001");
        order.setDeliveryCountry("USA");
        order.setDeliveryPhone(user.getPhoneNumber());
        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(5));
        order.setTrackingNumber("TRK" + orderNumber.replace("ORD-", ""));
        
        // Set default values for required fields to avoid NULL constraint violations
        order.setSubtotal(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setShippingAmount(BigDecimal.ZERO);
        
        return order;
    }

    /**
     * Creates an order item with the specified details
     */
    private OrderItem createOrderItem(Order order, Product product, Integer quantity) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage("/images/products/" + product.getSku().toLowerCase() + ".jpg");
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.calculateSubtotal();
        return item;
    }

    /**
     * Updates order totals based on order items
     */
    private void updateOrderTotals(List<Order> orders) {
        for (Order order : orders) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            BigDecimal subtotal = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            order.setSubtotal(subtotal);
            order.setTaxAmount(subtotal.multiply(new BigDecimal("0.1"))); // 10% tax
            order.setShippingAmount(new BigDecimal("15.00")); // Fixed shipping
            order.recalculateTotals();
            orderRepository.save(order);
        }
    }

    /**
     * Creates a payment with the specified details
     */
    private Payment createPayment(Order order, User user, Payment.PaymentStatus status, Payment.PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setUserId(user.getId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setStatus(status);
        payment.setTransactionId("TXN" + order.getOrderNumber().replace("ORD-", ""));
        payment.setGatewayResponse("Approved");
        payment.setCardLastFour("1234");
        payment.setCardBrand("VISA");
        payment.setCardExpiryMonth(12);
        payment.setCardExpiryYear(2026);
        payment.setBillingAddress(order.getDeliveryAddress());
        payment.setBillingCity(order.getDeliveryCity());
        payment.setBillingState(order.getDeliveryState());
        payment.setBillingZipCode(order.getDeliveryZipCode());
        payment.setBillingCountry(order.getDeliveryCountry());
        
        if (status == Payment.PaymentStatus.SUCCESSFUL) {
            payment.setProcessedAt(LocalDateTime.now());
        }
        
        return payment;
    }
}
