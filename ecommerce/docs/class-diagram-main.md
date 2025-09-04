# 🏗️ Ecommerce Backend Class Diagram

## 📊 Main Class Structure

```mermaid
classDiagram
    %% Main Application
    class EcommerceApplication {
        +main(String[] args)
    }

    %% Entity Classes
    class User {
        -String id, email, password, firstName, lastName, phoneNumber
        -UserRole role, boolean emailVerified, phoneVerified, active
        -LocalDateTime createdAt, updatedAt
        +getters() +setters()
    }

    class Product {
        -String id, name, description, category, subCategory, brand, model, sku
        -BigDecimal price, originalPrice, discountPercentage, weight
        -int stockQuantity, minStockLevel, reviewCount
        -BigDecimal averageRating, String dimensions, imageUrl
        -boolean featured, active, LocalDateTime createdAt, updatedAt
        +getters() +setters()
    }

    class Order {
        -String id, userId, orderNumber, deliveryAddress, paymentMethod, shippingMethod, notes
        -OrderStatus status, PaymentStatus paymentStatus
        -BigDecimal subtotal, taxAmount, shippingAmount, totalAmount
        -LocalDateTime orderDate, createdAt, updatedAt
        +getters() +setters()
    }

    class Payment {
        -String id, orderId, userId, transactionId, currency, gatewayResponse
        -PaymentStatus status, PaymentMethod paymentMethod
        -BigDecimal amount, LocalDateTime paymentDate, createdAt, updatedAt
        +getters() +setters()
    }

    %% Service Interfaces
    class UserService {
        <<interface>>
        +registerUser(), +loginUser(), +getUserProfile()
        +updateUserProfile(), +changePassword(), +forgotPassword()
        +resetPassword(), +checkEmailAvailability()
    }

    class CatalogService {
        <<interface>>
        +getProducts(), +getProductById(), +getCategories()
        +searchProducts(), +createProduct(), +updateProduct()
        +deleteProduct(), +updateInventory()
    }

    class OrderService {
        <<interface>>
        +createOrderFromCart(), +getOrderById(), +getOrderByOrderNumber()
        +getUserOrders(), +cancelOrder(), +updateOrderStatus()
        +updateOrderPaymentStatus()
    }

    class PaymentService {
        <<interface>>
        +processPayment(), +verifyPayment(), +getPaymentDetails()
        +getUserPaymentHistory(), +processRefund()
    }

    %% Service Implementations
    class UserServiceImpl {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -JwtService jwtService
        +registerUser(), +loginUser(), +getUserProfile()
        +updateUserProfile(), +changePassword(), +forgotPassword()
        +resetPassword(), +checkEmailAvailability()
    }

    class CatalogServiceImpl {
        -ProductRepository productRepository
        -CategoryRepository categoryRepository
        +getProducts(), +getProductById(), +getCategories()
        +searchProducts(), +createProduct(), +updateProduct()
        +deleteProduct(), +updateInventory()
    }

    class OrderServiceImpl {
        -OrderRepository orderRepository
        -OrderItemRepository orderItemRepository
        -CartService cartService
        +createOrderFromCart(), +getOrderById(), +getOrderByOrderNumber()
        +getUserOrders(), +cancelOrder(), +updateOrderStatus()
        +updateOrderPaymentStatus()
    }

    class PaymentServiceImpl {
        -PaymentRepository paymentRepository
        -OrderRepository orderRepository
        -PaymentGatewayService paymentGatewayService
        +processPayment(), +verifyPayment(), +getPaymentDetails()
        +getUserPaymentHistory(), +processRefund()
    }

    %% Repository Interfaces
    class UserRepository {
        <<interface>>
        +findByEmail(), +findByEmailAndActiveTrue(), +existsByEmail()
        +save(), +findById(), +findAll(), +deleteById(), +count()
    }

    class ProductRepository {
        <<interface>>
        +findByCategory(), +findByNameContainingIgnoreCase()
        +findByPriceBetween(), +findByFeaturedTrue(), +findByActiveTrue()
        +save(), +findById(), +findAll(), +deleteById()
    }

    class OrderRepository {
        <<interface>>
        +findByUserId(), +findByOrderNumber(), +save()
        +findById(), +findAll(), +deleteById()
    }

    class PaymentRepository {
        <<interface>>
        +findByOrderId(), +findByUserId(), +findByTransactionId()
        +save(), +findById(), +findAll(), +deleteById()
    }

    %% Controllers
    class UserController {
        -UserService userService
        +registerUser(), +loginUser(), +getUserProfile()
        +updateUserProfile(), +changePassword(), +forgotPassword()
        +resetPassword(), +checkEmailAvailability()
    }

    class CatalogController {
        -CatalogService catalogService
        +getProducts(), +getProductById(), +getCategories()
        +searchProducts(), +createProduct(), +updateProduct()
        +deleteProduct(), +updateInventory()
    }

    class OrderController {
        -OrderService orderService
        +createOrder(), +getOrderById(), +getOrderByOrderNumber()
        +getUserOrders(), +cancelOrder(), +updateOrderStatus()
    }

    class PaymentController {
        -PaymentService paymentService
        +processPayment(), +verifyPayment(), +getPaymentDetails()
        +getUserPaymentHistory(), +processRefund()
    }

    %% Specialized Services
    class OrderManagementService {
        -OrderService orderService
        -CartService cartService
        -OrderKafkaProducerService orderKafkaProducerService
        +createOrder(), +getOrderDetails(), +updateOrderStatus()
        +trackOrder(), +getOrderHistory(), +cancelOrder()
    }

    class PaymentGatewayService {
        <<interface>>
        +processPayment(), +verifyPayment(), +processRefund()
        +getPaymentStatus()
    }

    class StripePaymentGatewayService {
        -Stripe stripe, -String secretKey
        +processPayment(), +verifyPayment(), +processRefund()
        +getPaymentStatus()
    }

    %% Kafka Services
    class KafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendMessage(), +sendUserEvent()
    }

    class OrderKafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendOrderEvent(), +sendOrderStatusUpdate()
        +sendPaymentConfirmation()
    }

    class PaymentKafkaProducerService {
        -KafkaTemplate~String, String~ kafkaTemplate
        +sendPaymentEvent(), +sendPaymentConfirmation()
        +sendRefundProcessed()
    }

    %% Configuration
    class SecurityConfig {
        +passwordEncoder(), +securityFilterChain()
        +corsConfigurationSource()
    }

    class KafkaConfig {
        +producerFactory(), +kafkaTemplate()
        +userEventsTopic(), +orderEventsTopic()
        +paymentEventsTopic(), +testTopic()
    }

    %% Data Loading
    class DataLoader {
        -UserRepository userRepository
        -CategoryRepository categoryRepository
        -ProductRepository productRepository
        -CartRepository cartRepository
        -OrderRepository orderRepository
        -PaymentRepository paymentRepository
        +loadData(), -loadSampleData()
    }

    %% Enums
    class UserRole {
        <<enumeration>>
        USER, ADMIN, MODERATOR
    }

    class OrderStatus {
        <<enumeration>>
        PENDING, CONFIRMED, PROCESSING, SHIPPED
        DELIVERED, CANCELLED, RETURNED
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING, PROCESSING, COMPLETED, FAILED
        CANCELLED, REFUNDED
    }

    %% Relationships
    EcommerceApplication --> UserController
    EcommerceApplication --> CatalogController
    EcommerceApplication --> OrderController
    EcommerceApplication --> PaymentController

    %% Controller -> Service
    UserController --> UserService
    CatalogController --> CatalogService
    OrderController --> OrderService
    PaymentController --> PaymentService

    %% Service -> Implementation
    UserService --> UserServiceImpl
    CatalogService --> CatalogServiceImpl
    OrderService --> OrderServiceImpl
    PaymentService --> PaymentServiceImpl

    %% Service Implementation -> Repository
    UserServiceImpl --> UserRepository
    CatalogServiceImpl --> ProductRepository
    OrderServiceImpl --> OrderRepository
    PaymentServiceImpl --> PaymentRepository

    %% Specialized Services
    OrderManagementService --> OrderService
    OrderManagementService --> CartService
    PaymentGatewayService --> StripePaymentGatewayService

    %% Kafka Relationships
    OrderKafkaProducerService --> KafkaProducerService
    PaymentKafkaProducerService --> KafkaProducerService

    %% Configuration
    SecurityConfig --> UserService
    KafkaConfig --> OrderKafkaProducerService
    KafkaConfig --> PaymentKafkaProducerService

    %% Data Loading
    DataLoader --> UserRepository
    DataLoader --> ProductRepository
    DataLoader --> OrderRepository
    DataLoader --> PaymentRepository

    %% Entity Relationships
    User ||--o{ Order : "places"
    User ||--o{ Payment : "makes"
    Order ||--o{ Payment : "has"
    Product ||--o{ Order : "ordered_in"
```

## 🏗️ **Class Diagram Overview**

### 📋 **Core Components: 40+ Classes**

| Component | Classes | Purpose |
|-----------|---------|---------|
| **Entities** | 4 main classes | Data models with JPA annotations |
| **Repositories** | 4 interfaces | Data access layer |
| **Services** | 4 interfaces + implementations | Business logic layer |
| **Controllers** | 4 classes | REST API endpoints |
| **Specialized Services** | 3 classes | Order management & payment gateway |
| **Kafka Services** | 3 classes | Event streaming services |
| **Configuration** | 2 classes | App configuration & security |
| **Data Loading** | 1 class | Sample data initialization |
| **Enums** | 3 classes | Status & type definitions |

## 🔗 **Key Relationships**

### 1. **Layered Architecture** 🏛️
```
Controllers → Services → Repositories → Entities
```

### 2. **Service Dependencies** ⚙️
```
UserService → UserRepository
CatalogService → ProductRepository
OrderService → OrderRepository + CartService
PaymentService → PaymentRepository + PaymentGatewayService
```

### 3. **Event-Driven Communication** 📡
```
OrderManagementService → OrderKafkaProducerService
PaymentService → PaymentKafkaProducerService
```

## 🎯 **Design Patterns Used**

### ✅ **Implemented Patterns**
- **MVC Pattern** - Controllers, Services, Repositories
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **Event-Driven Architecture** - Kafka messaging
- **Dependency Injection** - Spring IoC container
- **Strategy Pattern** - Payment gateway abstraction

---

**Class Diagram Status: ✅ COMPLETE**

This class diagram represents the core architecture of your production-ready ecommerce backend with proper separation of concerns and scalable design patterns.
