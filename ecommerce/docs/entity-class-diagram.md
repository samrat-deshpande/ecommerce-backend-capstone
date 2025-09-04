# 🏗️ Ecommerce Entity Class Diagram

## 📊 Entity Relationships with JPA Mappings

```mermaid
classDiagram
    %% Core Entities
    class User {
        +String id
        +String email
        +String password
        +String firstName
        +String lastName
        +String phoneNumber
        +UserRole role
        +boolean active
        +boolean emailVerified
        +boolean phoneVerified
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +LocalDateTime lastLogin
    }

    class Category {
        +String id
        +String name
        +String description
        +String parentId
        +String imageUrl
        +String iconClass
        +Integer sortOrder
        +Integer productCount
        +boolean active
        +boolean featured
        +String metaTitle
        +String metaDescription
        +String metaKeywords
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class Product {
        +String id
        +String name
        +String description
        +BigDecimal price
        +BigDecimal originalPrice
        +Double discountPercentage
        +String category
        +String subCategory
        +String brand
        +String model
        +String sku
        +Integer stockQuantity
        +Integer minStockLevel
        +Double weight
        +String dimensions
        +String imageUrl
        +List~String~ additionalImages
        +Double averageRating
        +Integer reviewCount
        +boolean featured
        +boolean active
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class Cart {
        +String id
        +String userId
        +BigDecimal totalAmount
        +Integer itemCount
        +CartStatus status
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +List~CartItem~ items
    }

    class CartItem {
        +String id
        +String cartId
        +String productId
        +String productName
        +String productImage
        +Integer quantity
        +BigDecimal unitPrice
        +BigDecimal subtotal
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +Cart cart
    }

    class Order {
        +String id
        +String orderNumber
        +String userId
        +BigDecimal subtotal
        +BigDecimal taxAmount
        +BigDecimal shippingAmount
        +BigDecimal totalAmount
        +OrderStatus status
        +PaymentStatus paymentStatus
        +PaymentMethod paymentMethod
        +String paymentTransactionId
        +String deliveryAddress
        +String deliveryCity
        +String deliveryState
        +String deliveryZipCode
        +String deliveryCountry
        +String deliveryPhone
        +LocalDateTime estimatedDeliveryDate
        +String trackingNumber
        +String notes
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +List~OrderItem~ items
    }

    class OrderItem {
        +String id
        +String orderId
        +String productId
        +String productName
        +String productImage
        +Integer quantity
        +BigDecimal unitPrice
        +BigDecimal subtotal
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +Order order
    }

    class Payment {
        +String id
        +String orderId
        +String userId
        +BigDecimal amount
        +PaymentMethod paymentMethod
        +PaymentStatus status
        +String transactionId
        +String gatewayResponse
        +String gatewayErrorCode
        +String gatewayErrorMessage
        +String cardLastFour
        +String cardBrand
        +Integer cardExpiryMonth
        +Integer cardExpiryYear
        +String billingAddress
        +String billingCity
        +String billingState
        +String billingZipCode
        +String billingCountry
        +LocalDateTime processedAt
        +String failureReason
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class PasswordResetToken {
        +String id
        +String token
        +String userId
        +LocalDateTime expiryDate
        +boolean used
        +LocalDateTime createdAt
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
        EXPIRED
    }

    class OrderStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        PROCESSING
        SHIPPED
        DELIVERED
        CANCELLED
        REFUNDED
    }

    class PaymentStatus {
        <<enumeration>>
        PENDING
        AUTHORIZED
        PAID
        FAILED
        REFUNDED
        PARTIALLY_REFUNDED
    }

    class PaymentMethod {
        <<enumeration>>
        CREDIT_CARD
        DEBIT_CARD
        BANK_TRANSFER
        DIGITAL_WALLET
        CASH_ON_DELIVERY
    }

    %% Relationships
    %% User Relationships
    User ||--o{ Cart : "owns"
    User ||--o{ Order : "places"
    User ||--o{ Payment : "makes"
    User ||--o{ PasswordResetToken : "has"

    %% Category Relationships (Self-referencing)
    Category ||--o{ Category : "parent-child"

    %% Product Relationships
    Product ||--o{ CartItem : "added to cart"
    Product ||--o{ OrderItem : "ordered"

    %% Cart Relationships
    Cart ||--o{ CartItem : "contains"
    CartItem }o--|| Cart : "belongs to"

    %% Order Relationships
    Order ||--o{ OrderItem : "contains"
    OrderItem }o--|| Order : "belongs to"
    Order ||--o{ Payment : "has"

    %% Payment Relationships
    Payment }o--|| Order : "for"

    %% Enum Relationships
    User --> UserRole : "has"
    Cart --> CartStatus : "has"
    Order --> OrderStatus : "has"
    Order --> PaymentStatus : "has"
    Order --> PaymentMethod : "uses"
    Payment --> PaymentMethod : "uses"
    Payment --> PaymentStatus : "has"

    %% Notes for JPA Mappings
    note for User "No explicit JPA relationships<br/>Uses String userId references"
    note for Category "Self-referencing via parentId<br/>No explicit JPA @ManyToOne"
    note for Product "String category reference<br/>@ElementCollection for images"
    note for Cart "@OneToMany(mappedBy='cart')<br/>CascadeType.ALL, orphanRemoval=true"
    note for CartItem "@ManyToOne(fetch=LAZY)<br/>@JoinColumn(name='cart_id')"
    note for Order "@OneToMany(mappedBy='order')<br/>CascadeType.ALL, orphanRemoval=true"
    note for OrderItem "@ManyToOne(fetch=LAZY)<br/>@JoinColumn(name='order_id')"
    note for Payment "No explicit JPA relationships<br/>Uses String orderId, userId"
    note for PasswordResetToken "No explicit JPA relationships<br/>Uses String userId"
```

## 🔗 Relationship Mapping Details

### **1. User Entity Relationships**
- **User → Cart**: One-to-Many (via `userId` String reference)
- **User → Order**: One-to-Many (via `userId` String reference)  
- **User → Payment**: One-to-Many (via `userId` String reference)
- **User → PasswordResetToken**: One-to-Many (via `userId` String reference)

### **2. Category Entity Relationships**
- **Category → Category**: Self-referencing (via `parentId` String reference)
- **Category → Product**: One-to-Many (via `category` String reference)

### **3. Product Entity Relationships**
- **Product → CartItem**: One-to-Many (via `productId` String reference)
- **Product → OrderItem**: One-to-Many (via `productId` String reference)
- **Product → ProductImages**: One-to-Many (via `@ElementCollection`)

### **4. Cart Entity Relationships**
- **Cart → CartItem**: One-to-Many with explicit JPA mapping
  ```java
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<CartItem> items;
  ```

### **5. CartItem Entity Relationships**
- **CartItem → Cart**: Many-to-One with explicit JPA mapping
  ```java
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;
  ```

### **6. Order Entity Relationships**
- **Order → OrderItem**: One-to-Many with explicit JPA mapping
  ```java
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderItem> items;
  ```
- **Order → Payment**: One-to-Many (via `orderId` String reference)

### **7. OrderItem Entity Relationships**
- **OrderItem → Order**: Many-to-One with explicit JPA mapping
  ```java
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
  ```

### **8. Payment Entity Relationships**
- **Payment → Order**: Many-to-One (via `orderId` String reference)
- **Payment → User**: Many-to-One (via `userId` String reference)

## 🎯 Key Design Patterns

### **1. String-Based References**
Most relationships use String IDs instead of JPA entity references for:
- **Performance**: Avoids N+1 query problems
- **Flexibility**: Easier to work with in REST APIs
- **Simplicity**: Reduces complexity in entity management

### **2. Explicit JPA Mappings**
Only critical relationships use explicit JPA mappings:
- **Cart ↔ CartItem**: Bidirectional with cascade operations
- **Order ↔ OrderItem**: Bidirectional with cascade operations

### **3. Collection Tables**
- **Product → ProductImages**: Uses `@ElementCollection` for simple string lists

### **4. Enum Usage**
All status and type fields use enums for type safety:
- **UserRole**: USER, ADMIN, MODERATOR
- **CartStatus**: ACTIVE, CONVERTED, EXPIRED
- **OrderStatus**: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
- **PaymentStatus**: PENDING, AUTHORIZED, PAID, FAILED, REFUNDED, PARTIALLY_REFUNDED
- **PaymentMethod**: CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, DIGITAL_WALLET, CASH_ON_DELIVERY

## 📋 Entity Summary

| Entity | Primary Key | Key Relationships | JPA Mappings |
|--------|-------------|-------------------|--------------|
| **User** | String id (UUID) | → Cart, Order, Payment, PasswordResetToken | None (String refs) |
| **Category** | String id (UUID) | → Category (self), Product | None (String refs) |
| **Product** | String id (UUID) | → CartItem, OrderItem, ProductImages | @ElementCollection |
| **Cart** | String id (UUID) | → CartItem | @OneToMany |
| **CartItem** | String id (UUID) | → Cart, Product | @ManyToOne |
| **Order** | String id (UUID) | → OrderItem, Payment | @OneToMany |
| **OrderItem** | String id (UUID) | → Order, Product | @ManyToOne |
| **Payment** | String id (UUID) | → Order, User | None (String refs) |
| **PasswordResetToken** | String id (UUID) | → User | None (String refs) |

This design provides a clean separation between entities while maintaining referential integrity through foreign key constraints at the database level.
