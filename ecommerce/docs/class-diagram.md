# 🏗️ Ecommerce Backend Class Diagram

## 📊 UML Class Diagram

```mermaid
classDiagram
    %% Main Application
    class EcommerceApplication {
        +main(String[] args)
    }

    %% Entity Classes
    class User {
        -String id
        -String email
        -String password
        -String firstName
        -String lastName
        -String phoneNumber
        -UserRole role
        -boolean emailVerified
        -boolean phoneVerified
        -boolean active
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getEmail()
        +setEmail(String email)
        +getPassword()
        +setPassword(String password)
        +getFirstName()
        +setFirstName(String firstName)
        +getLastName()
        +setLastName(String lastName)
        +getPhoneNumber()
        +setPhoneNumber(String phoneNumber)
        +getRole()
        +setRole(UserRole role)
        +isEmailVerified()
        +setEmailVerified(boolean emailVerified)
        +isPhoneVerified()
        +setPhoneVerified(boolean phoneVerified)
        +isActive()
        +setActive(boolean active)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class Category {
        -String id
        -String name
        -String description
        -String parentId
        -boolean featured
        -boolean active
        -int sortOrder
        -int productCount
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getName()
        +setName(String name)
        +getDescription()
        +setDescription(String description)
        +getParentId()
        +setParentId(String parentId)
        +isFeatured()
        +setFeatured(boolean featured)
        +isActive()
        +setActive(boolean active)
        +getSortOrder()
        +setSortOrder(int sortOrder)
        +getProductCount()
        +setProductCount(int productCount)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class Product {
        -String id
        -String name
        -String description
        -BigDecimal price
        -BigDecimal originalPrice
        -BigDecimal discountPercentage
        -String category
        -String subCategory
        -String brand
        -String model
        -String sku
        -int stockQuantity
        -int minStockLevel
        -int reviewCount
        -BigDecimal averageRating
        -BigDecimal weight
        -String dimensions
        -String imageUrl
        -boolean featured
        -boolean active
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getName()
        +setName(String name)
        +getDescription()
        +setDescription(String description)
        +getPrice()
        +setPrice(BigDecimal price)
        +getOriginalPrice()
        +setOriginalPrice(BigDecimal originalPrice)
        +getDiscountPercentage()
        +setDiscountPercentage(BigDecimal discountPercentage)
        +getCategory()
        +setCategory(String category)
        +getSubCategory()
        +setSubCategory(String subCategory)
        +getBrand()
        +setBrand(String brand)
        +getModel()
        +setModel(String model)
        +getSku()
        +setSku(String sku)
        +getStockQuantity()
        +setStockQuantity(int stockQuantity)
        +getMinStockLevel()
        +setMinStockLevel(int minStockLevel)
        +getReviewCount()
        +setReviewCount(int reviewCount)
        +getAverageRating()
        +setAverageRating(BigDecimal averageRating)
        +getWeight()
        +setWeight(BigDecimal weight)
        +getDimensions()
        +setDimensions(String dimensions)
        +getImageUrl()
        +setImageUrl(String imageUrl)
        +isFeatured()
        +setFeatured(boolean featured)
        +isActive()
        +setActive(boolean active)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class Cart {
        -String id
        -String userId
        -CartStatus status
        -BigDecimal subtotal
        -BigDecimal taxAmount
        -BigDecimal totalAmount
        -String discountCode
        -BigDecimal discountAmount
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getUserId()
        +setUserId(String userId)
        +getStatus()
        +setStatus(CartStatus status)
        +getSubtotal()
        +setSubtotal(BigDecimal subtotal)
        +getTaxAmount()
        +setTaxAmount(BigDecimal taxAmount)
        +getTotalAmount()
        +setTotalAmount(BigDecimal totalAmount)
        +getDiscountCode()
        +setDiscountCode(String discountCode)
        +getDiscountAmount()
        +setDiscountAmount(BigDecimal discountAmount)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class CartItem {
        -String id
        -String cartId
        -String productId
        -int quantity
        -BigDecimal unitPrice
        -BigDecimal totalPrice
        -LocalDateTime createdAt
        +getId()
        +setId(String id)
        +getCartId()
        +setCartId(String cartId)
        +getProductId()
        +setProductId(String productId)
        +getQuantity()
        +setQuantity(int quantity)
        +getUnitPrice()
        +setUnitPrice(BigDecimal unitPrice)
        +getTotalPrice()
        +setTotalPrice(BigDecimal totalPrice)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
    }

    class Order {
        -String id
        -String userId
        -String orderNumber
        -OrderStatus status
        -PaymentStatus paymentStatus
        -BigDecimal subtotal
        -BigDecimal taxAmount
        -BigDecimal shippingAmount
        -BigDecimal totalAmount
        -String deliveryAddress
        -String paymentMethod
        -String shippingMethod
        -String notes
        -LocalDateTime orderDate
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getUserId()
        +setUserId(String userId)
        +getOrderNumber()
        +setOrderNumber(String orderNumber)
        +getStatus()
        +setStatus(OrderStatus status)
        +getPaymentStatus()
        +setPaymentStatus(PaymentStatus paymentStatus)
        +getSubtotal()
        +setSubtotal(BigDecimal subtotal)
        +getTaxAmount()
        +setTaxAmount(BigDecimal taxAmount)
        +getShippingAmount()
        +setShippingAmount(BigDecimal shippingAmount)
        +getTotalAmount()
        +setTotalAmount(BigDecimal totalAmount)
        +getDeliveryAddress()
        +setDeliveryAddress(String deliveryAddress)
        +getPaymentMethod()
        +setPaymentMethod(String paymentMethod)
        +getShippingMethod()
        +setShippingMethod(String shippingMethod)
        +getNotes()
        +setNotes(String notes)
        +getOrderDate()
        +setOrderDate(LocalDateTime orderDate)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class OrderItem {
        -String id
        -String orderId
        -String productId
        -int quantity
        -BigDecimal unitPrice
        -BigDecimal totalPrice
        -LocalDateTime createdAt
        +getId()
        +setId(String id)
        +getOrderId()
        +setOrderId(String orderId)
        +getProductId()
        +setProductId(String productId)
        +getQuantity()
        +setQuantity(int quantity)
        +getUnitPrice()
        +setUnitPrice(BigDecimal unitPrice)
        +getTotalPrice()
        +setTotalPrice(BigDecimal totalPrice)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
    }

    class Payment {
        -String id
        -String orderId
        -String userId
        -String transactionId
        -PaymentStatus status
        -PaymentMethod paymentMethod
        -BigDecimal amount
        -String currency
        -String gatewayResponse
        -LocalDateTime paymentDate
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId()
        +setId(String id)
        +getOrderId()
        +setOrderId(String orderId)
        +getUserId()
        +setUserId(String userId)
        +getTransactionId()
        +setTransactionId(String transactionId)
        +getStatus()
        +setStatus(PaymentStatus status)
        +getPaymentMethod()
        +setPaymentMethod(PaymentMethod paymentMethod)
        +getAmount()
        +setAmount(BigDecimal amount)
        +getCurrency()
        +setCurrency(String currency)
        +getGatewayResponse()
        +setGatewayResponse(String gatewayResponse)
        +getPaymentDate()
        +setPaymentDate(LocalDateTime paymentDate)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
        +getUpdatedAt()
        +setUpdatedAt(LocalDateTime updatedAt)
    }

    class PasswordResetToken {
        -String id
        -String userId
        -String token
        -LocalDateTime expiryDate
        -LocalDateTime createdAt
        +getId()
        +setId(String id)
        +getUserId()
        +setUserId(String userId)
        +getToken()
        +setToken(String token)
        +getExpiryDate()
        +setExpiryDate(LocalDateTime expiryDate)
        +getCreatedAt()
        +setCreatedAt(LocalDateTime createdAt)
    }

    %% Repository Interfaces
    class UserRepository {
        <<interface>>
        +findByEmail(String email)
        +findByEmailAndActiveTrue(String email)
        +existsByEmail(String email)
        +save(User user)
        +findById(String id)
        +findAll()
        +deleteById(String id)
        +count()
    }

    class CategoryRepository {
        <<interface>>
        +findByParentIdIsNull()
        +findByParentId(String parentId)
        +findByFeaturedTrue()
        +findByActiveTrue()
        +save(Category category)
        +findById(String id)
        +findAll()
        +deleteById(String id)
    }

    class ProductRepository {
        <<interface>>
        +findByCategory(String category)
        +findByNameContainingIgnoreCase(String name)
        +findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice)
        +findByFeaturedTrue()
        +findByActiveTrue()
        +save(Product product)
        +findById(String id)
        +findAll()
        +deleteById(String id)
    }

    class CartRepository {
        <<interface>>
        +findByUserIdAndStatus(String userId, CartStatus status)
        +findByUserId(String userId)
        +save(Cart cart)
        +findById(String id)
        +deleteById(String id)
    }

    class CartItemRepository {
        <<interface>>
        +findByCartId(String cartId)
        +deleteByCartId(String cartId)
        +save(CartItem cartItem)
        +findById(String id)
        +deleteById(String id)
    }

    class OrderRepository {
        <<interface>>
        +findByUserId(String userId)
        +findByOrderNumber(String orderNumber)
        +save(Order order)
        +findById(String id)
        +findAll()
        +deleteById(String id)
    }

    class OrderItemRepository {
        <<interface>>
        +findByOrderId(String orderId)
        +save(OrderItem orderItem)
        +findById(String id)
        +deleteById(String id)
    }

    class PaymentRepository {
        <<interface>>
        +findByOrderId(String orderId)
        +findByUserId(String userId)
        +findByTransactionId(String transactionId)
        +save(Payment payment)
        +findById(String id)
        +findAll()
        +deleteById(String id)
    }

    class PasswordResetTokenRepository {
        <<interface>>
        +findByToken(String token)
        +findByUserId(String userId)
        +save(PasswordResetToken token)
        +deleteByUserId(String userId)
        +deleteByExpiryDateBefore(LocalDateTime date)
    }

    %% Service Interfaces
    class UserService {
        <<interface>>
        +registerUser(UserRegistrationRequest request)
        +loginUser(LoginRequest request)
        +getUserProfile(String userId)
        +updateUserProfile(String userId, UserUpdateRequest request)
        +changePassword(String userId, ChangePasswordRequest request)
        +forgotPassword(ForgotPasswordRequest request)
        +resetPassword(ResetPasswordRequest request)
        +checkEmailAvailability(String email)
    }

    class CatalogService {
        <<interface>>
        +getProducts(int page, int size, String categoryId, String searchTerm, Double minPrice, Double maxPrice)
        +getProductById(String productId)
        +getCategories()
        +searchProducts(String query, int page, int size)
        +getProductsByCategory(String categoryId, int page, int size)
        +getProductInventory(String productId)
        +createProduct(Map~String, Object~ productData)
        +updateProduct(String productId, Map~String, Object~ productData)
        +deleteProduct(String productId)
        +updateInventory(String productId, int quantity)
    }

    class CartService {
        <<interface>>
        +getUserCart(String userId)
        +addProductToCart(AddToCartRequest request)
        +updateCartItemQuantity(String cartItemId, int quantity)
        +removeItemFromCart(String cartItemId)
        +clearCart(String userId)
        +applyDiscountCode(String userId, String discountCode)
    }

    class OrderService {
        <<interface>>
        +createOrderFromCart(String userId, CreateOrderRequest request)
        +getOrderById(String orderId, String userId)
        +getOrderByOrderNumber(String orderNumber, String userId)
        +getUserOrders(String userId, int page, int size)
        +cancelOrder(String orderId, String userId)
        +updateOrderStatus(String orderId, UpdateOrderStatusRequest request)
        +updateOrderPaymentStatus(String orderId, PaymentStatus status)
    }

    class PaymentService {
        <<interface>>
        +processPayment(String orderId, String userId, BigDecimal amount, PaymentMethod paymentMethod, Map~String, Object~ paymentDetails)
        +verifyPayment(String transactionId, String orderId)
        +getPaymentDetails(String paymentId)
        +getUserPaymentHistory(String userId, int page, int size)
        +processRefund(String paymentId, RefundRequest request)
    }

    %% Service Implementations
    class UserServiceImpl {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -JwtService jwtService
        -EmailService emailService
        -PasswordResetTokenRepository passwordResetTokenRepository
        +registerUser(UserRegistrationRequest request)
        +loginUser(LoginRequest request)
        +getUserProfile(String userId)
        +updateUserProfile(String userId, UserUpdateRequest request)
        +changePassword(String userId, ChangePasswordRequest request)
        +forgotPassword(ForgotPasswordRequest request)
        +resetPassword(ResetPasswordRequest request)
        +checkEmailAvailability(String email)
        -User createUserFromRequest(UserRegistrationRequest request)
        -void sendPasswordResetEmail(User user, String token)
        -String generatePasswordResetToken()
    }

    class CatalogServiceImpl {
        -ProductRepository productRepository
        -CategoryRepository categoryRepository
        +getProducts(int page, int size, String categoryId, String searchTerm, Double minPrice, Double maxPrice)
        +getProductById(String productId)
        +getCategories()
        +searchProducts(String query, int page, int size)
        +getProductsByCategory(String categoryId, int page, int size)
        +getProductInventory(String productId)
        +createProduct(Map~String, Object~ productData)
        +updateProduct(String productId, Map~String, Object~ productData)
        +deleteProduct(String productId)
        +updateInventory(String productId, int quantity)
        -Map~String, Object~ buildProductResponse(Product product)
        -Map~String, Object~ buildCategoryResponse(Category category)
    }

    class CartServiceImpl {
        -CartRepository cartRepository
        -CartItemRepository cartItemRepository
        -ProductRepository productRepository
        +getUserCart(String userId)
        +addProductToCart(AddToCartRequest request)
        +updateCartItemQuantity(String cartItemId, int quantity)
        +removeItemFromCart(String cartItemId)
        +clearCart(String userId)
        +applyDiscountCode(String userId, String discountCode)
        -Cart getOrCreateCart(String userId)
        -void updateCartTotals(Cart cart)
        -BigDecimal calculateTax(BigDecimal subtotal)
    }

    class OrderServiceImpl {
        -OrderRepository orderRepository
        -OrderItemRepository orderItemRepository
        -CartService cartService
        -ProductRepository productRepository
        +createOrderFromCart(String userId, CreateOrderRequest request)
        +getOrderById(String orderId, String userId)
        +getOrderByOrderNumber(String orderNumber, String userId)
        +getUserOrders(String userId, int page, int size)
        +cancelOrder(String orderId, String userId)
        +updateOrderStatus(String orderId, UpdateOrderStatusRequest request)
        +updateOrderPaymentStatus(String orderId, PaymentStatus status)
        -Order createOrderFromCart(String userId, CreateOrderRequest request, Cart cart)
        -List~OrderItem~ createOrderItems(Cart cart)
        -String generateOrderNumber()
    }

    class PaymentServiceImpl {
        -PaymentRepository paymentRepository
        -OrderRepository orderRepository
        -PaymentGatewayService paymentGatewayService
        -PaymentKafkaProducerService paymentKafkaProducerService
        +processPayment(String orderId, String userId, BigDecimal amount, PaymentMethod paymentMethod, Map~String, Object~ paymentDetails)
        +verifyPayment(String transactionId, String orderId)
        +getPaymentDetails(String paymentId)
        +getUserPaymentHistory(String userId, int page, int size)
        +processRefund(String paymentId, RefundRequest request)
        -Payment createPayment(String orderId, String userId, BigDecimal amount, PaymentMethod paymentMethod)
        -void processPaymentWithGateway(Payment payment, Map~String, Object~ paymentDetails)
        -void sendPaymentEvent(Payment payment, String eventType)
    }

    %% Specialized Services
    class OrderManagementService {
        -OrderService orderService
        -CartService cartService
        -OrderKafkaProducerService orderKafkaProducerService
        -UserService userService
        +createOrder(String userId, CreateOrderRequest request)
        +getOrderDetails(String orderId, String userId)
        +updateOrderStatus(String orderId, UpdateOrderStatusRequest request)
        +trackOrder(String orderId, String userId)
        +getOrderHistory(String userId, int page, int size)
        +cancelOrder(String orderId, String userId)
        -void validateOrderRequest(CreateOrderRequest request)
        -void sendOrderEvent(Order order, String eventType)
        -void updateInventory(Order order)
    }

    class PaymentGatewayService {
        <<interface>>
        +processPayment(PaymentRequest request)
        +verifyPayment(String transactionId)
        +processRefund(RefundRequest request)
        +getPaymentStatus(String transactionId)
    }

    class StripePaymentGatewayService {
        -Stripe stripe
        -String secretKey
        +processPayment(PaymentRequest request)
        +verifyPayment(String transactionId)
        +processRefund(RefundRequest request)
        +getPaymentStatus(String transactionId)
        -PaymentIntent createPaymentIntent(PaymentRequest request)
        -Refund createRefund(RefundRequest request)
    }

    %% Kafka Services
    class KafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendMessage(String topic, String key, String message)
        +sendUserEvent(String eventType, User user)
    }

    class OrderKafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendOrderEvent(OrderEvent event)
        +sendOrderStatusUpdate(String orderId, OrderStatus status)
        +sendPaymentConfirmation(String orderId, PaymentStatus status)
    }

    class PaymentKafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendPaymentEvent(PaymentEvent event)
        +sendPaymentConfirmation(String orderId, PaymentStatus status)
        +sendRefundProcessed(String paymentId, RefundStatus status)
    }

    class OrderKafkaConsumerService {
        -OrderService orderService
        +handleOrderEvent(OrderEvent event)
        +handlePaymentStatusUpdate(PaymentEvent event)
        +handleUserVerification(UserEvent event)
    }

    class PaymentKafkaConsumerService {
        -PaymentService paymentService
        +handlePaymentVerificationRequest(String orderId)
        +handleRefundRequest(RefundEvent event)
    }

    %% Controllers
    class UserController {
        -UserService userService
        +registerUser(UserRegistrationRequest request)
        +loginUser(LoginRequest request)
        +getUserProfile(String userId)
        +updateUserProfile(String userId, UserUpdateRequest request)
        +changePassword(String userId, ChangePasswordRequest request)
        +forgotPassword(ForgotPasswordRequest request)
        +resetPassword(ResetPasswordRequest request)
        +checkEmailAvailability(String email)
    }

    class CatalogController {
        -CatalogService catalogService
        +getProducts(int page, int size, String categoryId, String searchTerm, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDir)
        +getProductById(String productId)
        +getCategories()
        +searchProducts(String query, int page, int size)
        +getProductsByCategory(String categoryId, int page, int size)
        +getProductInventory(String productId)
        +createProduct(Map~String, Object~ productData)
        +updateProduct(String productId, Map~String, Object~ productData)
        +deleteProduct(String productId)
        +updateInventory(String productId, int quantity)
    }

    class CartController {
        -CartService cartService
        +getUserCart(String userId)
        +addProductToCart(AddToCartRequest request)
        +updateCartItemQuantity(String cartItemId, int quantity)
        +removeItemFromCart(String cartItemId)
        +clearCart(String userId)
        +applyDiscountCode(String userId, String discountCode)
    }

    class OrderController {
        -OrderService orderService
        +createOrder(String userId, CreateOrderRequest request)
        +getOrderById(String orderId, String userId)
        +getOrderByOrderNumber(String orderNumber, String userId)
        +getUserOrders(String userId, int page, int size)
        +cancelOrder(String orderId, String userId)
        +updateOrderStatus(String orderId, UpdateOrderStatusRequest request)
    }

    class PaymentController {
        -PaymentService paymentService
        +processPayment(String orderId, String userId, BigDecimal amount, PaymentMethod paymentMethod, Map~String, Object~ paymentDetails)
        +verifyPayment(String transactionId, String orderId)
        +getPaymentDetails(String paymentId)
        +getUserPaymentHistory(String userId, int page, int size)
        +processRefund(String paymentId, RefundRequest request)
    }

    class HomeController {
        +home()
        +health()
    }

    class TestController {
        -KafkaProducerService kafkaProducerService
        +health()
        +info()
        +root()
        +testKafka()
    }

    %% Configuration Classes
    class SecurityConfig {
        -PasswordEncoder passwordEncoder()
        -SecurityFilterChain securityFilterChain(HttpSecurity http)
        -CorsConfigurationSource corsConfigurationSource()
    }

    class KafkaConfig {
        -ProducerFactory~String, String~ producerFactory()
        -KafkaTemplate~String, String~ kafkaTemplate()
        -NewTopic userEventsTopic()
        -NewTopic orderEventsTopic()
        -NewTopic paymentEventsTopic()
        -NewTopic paymentVerificationTopic()
        -NewTopic testTopic()
    }

    class TestConfig {
        -KafkaTemplate~String, String~ mockKafkaTemplate()
    }

    %% Data Loading
    class DataLoader {
        -UserRepository userRepository
        -CategoryRepository categoryRepository
        -ProductRepository productRepository
        -CartRepository cartRepository
        -CartItemRepository cartItemRepository
        -OrderRepository orderRepository
        -OrderItemRepository orderItemRepository
        -PaymentRepository paymentRepository
        -PasswordEncoder passwordEncoder
        +loadData()
        -void loadSampleData()
        -List~User~ loadUsers()
        -List~Category~ loadCategories()
        -List~Product~ loadProducts(List~Category~ categories)
        -void loadCartsAndItems(List~User~ users, List~Product~ products)
        -void loadOrdersAndPayments(List~User~ users, List~Product~ products)
    }

    %% Event Classes
    class OrderEvent {
        -String eventId
        -String orderId
        -String userId
        -String eventType
        -OrderStatus orderStatus
        -LocalDateTime timestamp
        -Map~String, Object~ eventData
        +getEventId()
        +setEventId(String eventId)
        +getOrderId()
        +setOrderId(String orderId)
        +getUserId()
        +setUserId(String userId)
        +getEventType()
        +setEventType(String eventType)
        +getOrderStatus()
        +setOrderStatus(OrderStatus orderStatus)
        +getTimestamp()
        +setTimestamp(LocalDateTime timestamp)
        +getEventData()
        +setEventData(Map~String, Object~ eventData)
    }

    class PaymentEvent {
        -String eventId
        -String paymentId
        -String orderId
        -String userId
        -String eventType
        -PaymentStatus paymentStatus
        -BigDecimal amount
        -LocalDateTime timestamp
        -Map~String, Object~ eventData
        +getEventId()
        +setEventId(String eventId)
        +getPaymentId()
        +setPaymentId(String paymentId)
        +getOrderId()
        +setOrderId(String orderId)
        +getUserId()
        +setUserId(String userId)
        +getEventType()
        +setEventType(String eventType)
        +getPaymentStatus()
        +setPaymentStatus(PaymentStatus paymentStatus)
        +getAmount()
        +setAmount(BigDecimal amount)
        +getTimestamp()
        +setTimestamp(LocalDateTime timestamp)
        +getEventData()
        +setEventData(Map~String, Object~ eventData)
    }

    %% Utility Services
    class JwtService {
        -String secretKey
        -long expirationTime
        +generateToken(UserDetails userDetails)
        +generateToken(Map~String, Object~ claims, UserDetails userDetails)
        +extractUsername(String token)
        +extractExpiration(String token)
        +extractClaim(String token, Function~Claims, T~ claimsResolver)
        +extractAllClaims(String token)
        +isTokenExpired(String token)
        +validateToken(String token, UserDetails userDetails)
        +isTokenValid(String token, UserDetails userDetails)
    }

    class EmailService {
        -JavaMailSender mailSender
        -String fromEmail
        +sendPasswordResetEmail(String to, String token)
        +sendWelcomeEmail(String to, String firstName)
        +sendOrderConfirmationEmail(String to, String orderNumber)
        -SimpleMailMessage createMailMessage(String to, String subject, String text)
    }

    %% Enums
    class UserRole {
        <<enumeration>>
        USER
        ADMIN
        MODERATOR
    }

    class CartStatus {
        <<enumeration>>
        ACTIVE
        CONVERTED
        ABANDONED
    }

    class OrderStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        PROCESSING
        SHIPPED
        DELIVERED
        CANCELLED
        RETURNED
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING
        PROCESSING
        COMPLETED
        FAILED
        CANCELLED
        REFUNDED
    }

    class PaymentMethod {
        <<enumeration>>
        CREDIT_CARD
        DEBIT_CARD
        PAYPAL
        BANK_TRANSFER
        CASH_ON_DELIVERY
    }

    %% Relationships
    EcommerceApplication --> UserController
    EcommerceApplication --> CatalogController
    EcommerceApplication --> CartController
    EcommerceApplication --> OrderController
    EcommerceApplication --> PaymentController
    EcommerceApplication --> HomeController
    EcommerceApplication --> TestController

    %% Entity Relationships
    User ||--o{ Cart : "owns"
    User ||--o{ Order : "places"
    User ||--o{ Payment : "makes"
    User ||--o{ PasswordResetToken : "has"

    Category ||--o{ Category : "parent-child"
    Category ||--o{ Product : "contains"

    Product ||--o{ CartItem : "added_to"
    Product ||--o{ OrderItem : "ordered_in"

    Cart ||--o{ CartItem : "contains"
    Order ||--o{ OrderItem : "contains"
    Order ||--o{ Payment : "has"

    %% Repository Relationships
    UserController --> UserService
    CatalogController --> CatalogService
    CartController --> CartService
    OrderController --> OrderService
    PaymentController --> PaymentService

    UserService --> UserServiceImpl
    CatalogService --> CatalogServiceImpl
    CartService --> CartServiceImpl
    OrderService --> OrderServiceImpl
    PaymentService --> PaymentServiceImpl

    %% Service Implementation Relationships
    UserServiceImpl --> UserRepository
    UserServiceImpl --> PasswordResetTokenRepository
    CatalogServiceImpl --> ProductRepository
    CatalogServiceImpl --> CategoryRepository
    CartServiceImpl --> CartRepository
    CartServiceImpl --> CartItemRepository
    CartServiceImpl --> ProductRepository
    OrderServiceImpl --> OrderRepository
    OrderServiceImpl --> OrderItemRepository
    OrderServiceImpl --> CartService
    PaymentServiceImpl --> PaymentRepository
    PaymentServiceImpl --> OrderRepository
    PaymentServiceImpl --> PaymentGatewayService

    %% Specialized Service Relationships
    OrderManagementService --> OrderService
    OrderManagementService --> CartService
    OrderManagementService --> OrderKafkaProducerService
    OrderManagementService --> UserService

    PaymentGatewayService --> StripePaymentGatewayService

    %% Kafka Relationships
    OrderKafkaProducerService --> KafkaProducerService
    PaymentKafkaProducerService --> KafkaProducerService
    OrderKafkaConsumerService --> OrderService
    PaymentKafkaConsumerService --> PaymentService

    %% Configuration Relationships
    SecurityConfig --> UserService
    KafkaConfig --> OrderKafkaProducerService
    KafkaConfig --> PaymentKafkaProducerService
    TestConfig --> TestController

    %% Data Loading Relationships
    DataLoader --> UserRepository
    DataLoader --> CategoryRepository
    DataLoader --> ProductRepository
    DataLoader --> CartRepository
    DataLoader --> CartItemRepository
    DataLoader --> OrderRepository
    DataLoader --> OrderItemRepository
    DataLoader --> PaymentRepository
    DataLoader --> PasswordResetTokenRepository

    %% Event Relationships
    OrderEvent --> Order
    PaymentEvent --> Payment
    OrderEvent --> User

    %% Enum Relationships
    User --> UserRole
    Cart --> CartStatus
    Order --> OrderStatus
    Payment --> PaymentStatus
    Payment --> PaymentMethod
```

## 🏗️ **Class Diagram Overview**

### 📋 **Core Components: 50+ Classes**

| Component | Classes | Purpose |
|-----------|---------|---------|
| **Entities** | 9 classes | Data models with JPA annotations |
| **Repositories** | 9 interfaces | Data access layer |
| **Services** | 8 interfaces + implementations | Business logic layer |
| **Controllers** | 6 classes | REST API endpoints |
| **Configuration** | 3 classes | App configuration & security |
| **Kafka** | 5 classes | Event streaming services |
| **Events** | 2 classes | Domain event models |
| **Utilities** | 2 classes | JWT & Email services |
| **Enums** | 5 classes | Status & type definitions |

## 🔗 **Key Relationships**

### 1. **Layered Architecture** 🏛️
```
Controllers → Services → Repositories → Entities
```

### 2. **Service Dependencies** ⚙️
```
UserService → UserRepository + PasswordResetTokenRepository
CatalogService → ProductRepository + CategoryRepository
CartService → CartRepository + CartItemRepository + ProductRepository
OrderService → OrderRepository + OrderItemRepository + CartService
PaymentService → PaymentRepository + OrderRepository + PaymentGatewayService
```

### 3. **Event-Driven Communication** 📡
```
OrderManagementService → OrderKafkaProducerService
PaymentService → PaymentKafkaProducerService
KafkaConsumers → Services (for event handling)
```

### 4. **Configuration Management** ⚙️
```
SecurityConfig → UserService (for authentication)
KafkaConfig → Kafka services (for messaging)
TestConfig → TestController (for testing)
```

## 🎯 **Design Patterns Used**

### ✅ **Implemented Patterns**
- **MVC Pattern** - Controllers, Services, Repositories
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **Event-Driven Architecture** - Kafka messaging
- **Dependency Injection** - Spring IoC container
- **Strategy Pattern** - Payment gateway abstraction
- **Observer Pattern** - Kafka event publishing
- **Factory Pattern** - Object creation in services

### 🔮 **Future Enhancements**
- **Command Pattern** - For complex operations
- **Chain of Responsibility** - For order processing
- **State Pattern** - For order/payment status management
- **Template Method** - For common service operations

## 🚀 **Architecture Benefits**

### **Scalability** 📈
- **Microservices-ready** architecture
- **Event-driven** communication
- **Repository abstraction** for data access
- **Service layer** for business logic separation

### **Maintainability** 🔧
- **Clear separation** of concerns
- **Interface-based** design
- **Dependency injection** for loose coupling
- **Consistent naming** conventions

### **Testability** 🧪
- **Mockable interfaces** for unit testing
- **Test configuration** classes
- **H2 database** for integration testing
- **Kafka mocking** for event testing

---

**Class Diagram Status: ✅ COMPLETE & COMPREHENSIVE**

This class diagram represents a production-ready, enterprise-grade ecommerce backend with proper separation of concerns, event-driven architecture, and scalable design patterns.
