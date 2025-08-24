# 🛒 Cart & Checkout, Order Management & Payment API Documentation

## 📋 **Overview**
This document describes the complete API endpoints for the ecommerce platform's Cart & Checkout, Order Management, and Payment systems.

---

## 🛒 **1. Cart & Checkout System**

### **1.1 Get User's Cart**
- **Endpoint**: `GET /api/cart?userId={userId}`
- **Description**: Retrieve the user's active shopping cart with all items
- **Authentication**: Required
- **Response**: Cart details with items, quantities, and totals

### **1.2 Add Product to Cart**
- **Endpoint**: `POST /api/cart/add?userId={userId}`
- **Description**: Add a product to the user's cart
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "productId": "uuid-string",
    "quantity": 2
  }
  ```
- **Response**: Updated cart details

### **1.3 Update Cart Item Quantity**
- **Endpoint**: `PUT /api/cart/items/{itemId}?userId={userId}`
- **Description**: Update the quantity of a specific cart item
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "quantity": 3
  }
  ```
- **Response**: Updated cart details

### **1.4 Remove Item from Cart**
- **Endpoint**: `DELETE /api/cart/items/{itemId}?userId={userId}`
- **Description**: Remove a specific item from the cart
- **Authentication**: Required
- **Response**: Updated cart details

### **1.5 Clear Cart**
- **Endpoint**: `DELETE /api/cart/clear?userId={userId}`
- **Description**: Remove all items from the user's cart
- **Authentication**: Required
- **Response**: Confirmation message

### **1.6 Get Cart Summary**
- **Endpoint**: `GET /api/cart/summary?userId={userId}`
- **Description**: Get cart summary (item count, total amount)
- **Authentication**: Required
- **Response**: Cart summary information

### **1.7 Check Product in Cart**
- **Endpoint**: `GET /api/cart/check/{productId}?userId={userId}`
- **Description**: Check if a specific product exists in the user's cart
- **Authentication**: Required
- **Response**: Boolean indicating if product is in cart

### **1.8 Apply Discount**
- **Endpoint**: `POST /api/cart/discount?userId={userId}`
- **Description**: Apply a discount code to the cart
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "discountCode": "SAVE20"
  }
  ```
- **Response**: Cart with applied discount

### **1.9 Remove Discount**
- **Endpoint**: `DELETE /api/cart/discount?userId={userId}`
- **Description**: Remove applied discount from the cart
- **Authentication**: Required
- **Response**: Cart without discount

### **1.10 Validate Cart**
- **Endpoint**: `POST /api/cart/validate?userId={userId}`
- **Description**: Validate cart items (check stock, prices)
- **Authentication**: Required
- **Response**: Validation results

### **1.11 Checkout Cart**
- **Endpoint**: `POST /api/cart/checkout?userId={userId}`
- **Description**: Convert cart to order and process checkout
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "deliveryAddress": "123 Main St",
    "deliveryCity": "New York",
    "deliveryState": "NY",
    "deliveryZipCode": "10001",
    "deliveryCountry": "USA",
    "deliveryPhone": "+1234567890",
    "paymentMethod": "CREDIT_CARD"
  }
  ```
- **Response**: Order creation confirmation

---

## 📦 **2. Order Management System**

### **2.1 Create Order**
- **Endpoint**: `POST /api/orders?userId={userId}`
- **Description**: Create a new order from cart
- **Authentication**: Required
- **Request Body**: Order data including delivery address and payment method
- **Response**: Created order details

### **2.2 Get Order by ID**
- **Endpoint**: `GET /api/orders/{orderId}?userId={userId}`
- **Description**: Retrieve order details by order ID
- **Authentication**: Required
- **Response**: Complete order information

### **2.3 Get Order by Order Number**
- **Endpoint**: `GET /api/orders/number/{orderNumber}?userId={userId}`
- **Description**: Retrieve order details by order number
- **Authentication**: Required
- **Response**: Complete order information

### **2.4 Get User's Order History**
- **Endpoint**: `GET /api/orders/user/{userId}?page={page}&size={size}`
- **Description**: Get paginated list of user's orders
- **Authentication**: Required
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
- **Response**: Paginated list of orders

### **2.5 Get All User Orders**
- **Endpoint**: `GET /api/orders/user/{userId}/all`
- **Description**: Get all user's orders without pagination
- **Authentication**: Required
- **Response**: Complete list of user's orders

### **2.6 Get Orders by Status**
- **Endpoint**: `GET /api/orders/user/{userId}/status/{status}`
- **Description**: Get user's orders filtered by status
- **Authentication**: Required
- **Response**: List of orders with specified status

### **2.7 Update Order Status**
- **Endpoint**: `PUT /api/orders/{orderId}/status?status={status}&userId={userId}`
- **Description**: Update the status of an order
- **Authentication**: Required
- **Response**: Updated order details

### **2.8 Cancel Order**
- **Endpoint**: `POST /api/orders/{orderId}/cancel?userId={userId}`
- **Description**: Cancel an existing order
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "reason": "Changed my mind"
  }
  ```
- **Response**: Cancelled order details

### **2.9 Get Order Tracking**
- **Endpoint**: `GET /api/orders/{orderId}/tracking?userId={userId}`
- **Description**: Get tracking information for an order
- **Authentication**: Required
- **Response**: Order tracking details

### **2.10 Track Order by Number (Public)**
- **Endpoint**: `GET /api/orders/track/{trackingNumber}`
- **Description**: Track order using tracking number (public endpoint)
- **Authentication**: Not required
- **Response**: Order tracking information

### **2.11 Update Order Tracking**
- **Endpoint**: `PUT /api/orders/{orderId}/tracking?userId={userId}`
- **Description**: Update tracking information for an order
- **Authentication**: Required
- **Request Body**: Tracking information
- **Response**: Updated tracking details

### **2.12 Get Order Confirmation**
- **Endpoint**: `GET /api/orders/{orderId}/confirmation?userId={userId}`
- **Description**: Get order confirmation details
- **Authentication**: Required
- **Response**: Order confirmation information

### **2.13 Resend Order Confirmation**
- **Endpoint**: `POST /api/orders/{orderId}/resend-confirmation?userId={userId}`
- **Description**: Resend order confirmation email
- **Authentication**: Required
- **Response**: Email status

### **2.14 Get User Order Statistics**
- **Endpoint**: `GET /api/orders/user/{userId}/stats`
- **Description**: Get order statistics for the user
- **Authentication**: Required
- **Response**: Order statistics

### **2.15 Admin: Get Order Details**
- **Endpoint**: `GET /api/orders/admin/{orderId}`
- **Description**: Get complete order details for admin
- **Authentication**: Required (Admin)
- **Response**: Complete order information

### **2.16 Admin: Get All Orders**
- **Endpoint**: `GET /api/orders/admin?page={page}&size={size}&status={status}&userId={userId}&startDate={startDate}&endDate={endDate}`
- **Description**: Get all orders with filtering and pagination
- **Authentication**: Required (Admin)
- **Parameters**: Various filter options
- **Response**: Paginated list of all orders

### **2.17 Update Order Delivery**
- **Endpoint**: `PUT /api/orders/{orderId}/delivery?userId={userId}`
- **Description**: Update delivery information for an order
- **Authentication**: Required
- **Request Body**: Delivery information
- **Response**: Updated delivery details

### **2.18 Request Order Refund**
- **Endpoint**: `POST /api/orders/{orderId}/refund?userId={userId}`
- **Description**: Request a refund for an order
- **Authentication**: Required
- **Request Body**: Refund request details
- **Response**: Refund request confirmation

### **2.19 Get Order Invoice**
- **Endpoint**: `GET /api/orders/{orderId}/invoice?userId={userId}`
- **Description**: Get order invoice details
- **Authentication**: Required
- **Response**: Order invoice information

---

## 💳 **3. Payment System**

### **3.1 Process Payment**
- **Endpoint**: `POST /api/payments/process?orderId={orderId}&userId={userId}`
- **Description**: Process payment for an order
- **Authentication**: Required
- **Request Body**: Payment information
- **Response**: Payment processing result

### **3.2 Process Credit Card Payment**
- **Endpoint**: `POST /api/payments/credit-card?orderId={orderId}&userId={userId}`
- **Description**: Process credit card payment
- **Authentication**: Required
- **Request Body**: Credit card information
- **Response**: Payment processing result

### **3.3 Process Digital Wallet Payment**
- **Endpoint**: `POST /api/payments/digital-wallet?orderId={orderId}&userId={userId}`
- **Description**: Process digital wallet payment
- **Authentication**: Required
- **Request Body**: Digital wallet information
- **Response**: Payment processing result

### **3.4 Process Bank Transfer Payment**
- **Endpoint**: `POST /api/payments/bank-transfer?orderId={orderId}&userId={userId}`
- **Description**: Process bank transfer payment
- **Authentication**: Required
- **Request Body**: Bank transfer information
- **Response**: Payment processing result

### **3.5 Get Payment Details**
- **Endpoint**: `GET /api/payments/{paymentId}?userId={userId}`
- **Description**: Get payment details by payment ID
- **Authentication**: Required
- **Response**: Payment details

### **3.6 Get Payment by Transaction ID**
- **Endpoint**: `GET /api/payments/transaction/{transactionId}`
- **Description**: Get payment details by transaction ID
- **Authentication**: Not required
- **Response**: Payment details

### **3.7 Get User Payment History**
- **Endpoint**: `GET /api/payments/user/{userId}?page={page}&size={size}`
- **Description**: Get paginated payment history for user
- **Authentication**: Required
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
- **Response**: Paginated list of payments

### **3.8 Get All User Payments**
- **Endpoint**: `GET /api/payments/user/{userId}/all`
- **Description**: Get all user's payments without pagination
- **Authentication**: Required
- **Response**: Complete list of user's payments

### **3.9 Get Payments by Status**
- **Endpoint**: `GET /api/payments/user/{userId}/status/{status}`
- **Description**: Get user's payments filtered by status
- **Authentication**: Required
- **Response**: List of payments with specified status

### **3.10 Get Payments by Method**
- **Endpoint**: `GET /api/payments/user/{userId}/method/{paymentMethod}`
- **Description**: Get user's payments filtered by payment method
- **Authentication**: Required
- **Response**: List of payments with specified method

### **3.11 Refund Payment**
- **Endpoint**: `POST /api/payments/{paymentId}/refund?userId={userId}`
- **Description**: Process a full refund for a payment
- **Authentication**: Required
- **Request Body**: Refund details
- **Response**: Refund result

### **3.12 Partial Refund Payment**
- **Endpoint**: `POST /api/payments/{paymentId}/partial-refund?userId={userId}`
- **Description**: Process a partial refund for a payment
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "amount": 50.00,
    "reason": "Damaged item"
  }
  ```
- **Response**: Partial refund result

### **3.13 Cancel Payment**
- **Endpoint**: `POST /api/payments/{paymentId}/cancel?userId={userId}`
- **Description**: Cancel a pending payment
- **Authentication**: Required
- **Request Body**:
  ```json
  {
    "reason": "Order cancelled"
  }
  ```
- **Response**: Cancellation result

### **3.14 Validate Payment Method**
- **Endpoint**: `POST /api/payments/validate/{paymentMethod}`
- **Description**: Validate payment method data
- **Authentication**: Not required
- **Request Body**: Payment data to validate
- **Response**: Validation result

### **3.15 Validate Credit Card**
- **Endpoint**: `POST /api/payments/validate/credit-card`
- **Description**: Validate credit card information
- **Authentication**: Not required
- **Request Body**: Credit card data
- **Response**: Validation result

### **3.16 Get Payment Receipt**
- **Endpoint**: `GET /api/payments/{paymentId}/receipt?userId={userId}`
- **Description**: Get payment receipt details
- **Authentication**: Required
- **Response**: Payment receipt information

### **3.17 Resend Payment Receipt**
- **Endpoint**: `POST /api/payments/{paymentId}/resend-receipt?userId={userId}`
- **Description**: Resend payment receipt email
- **Authentication**: Required
- **Response**: Email status

### **3.18 Get User Payment Statistics**
- **Endpoint**: `GET /api/payments/user/{userId}/stats`
- **Description**: Get payment statistics for the user
- **Authentication**: Required
- **Response**: Payment statistics

### **3.19 Admin: Get Payment Details**
- **Endpoint**: `GET /api/payments/admin/{paymentId}`
- **Description**: Get complete payment details for admin
- **Authentication**: Required (Admin)
- **Response**: Complete payment information

### **3.20 Admin: Get All Payments**
- **Endpoint**: `GET /api/payments/admin?page={page}&size={size}&status={status}&userId={userId}&paymentMethod={paymentMethod}&startDate={startDate}&endDate={endDate}`
- **Description**: Get all payments with filtering and pagination
- **Authentication**: Required (Admin)
- **Parameters**: Various filter options
- **Response**: Paginated list of all payments

### **3.21 Retry Failed Payment**
- **Endpoint**: `POST /api/payments/{paymentId}/retry?userId={userId}`
- **Description**: Retry a failed payment
- **Authentication**: Required
- **Response**: Retry result

### **3.22 Get Available Payment Methods**
- **Endpoint**: `GET /api/payments/methods`
- **Description**: Get list of available payment methods
- **Authentication**: Not required
- **Response**: Available payment methods

### **3.23 Check Payment Gateway Status**
- **Endpoint**: `GET /api/payments/gateway/status`
- **Description**: Check payment gateway health status
- **Authentication**: Not required
- **Response**: Gateway status information

### **3.24 Calculate Payment Fees**
- **Endpoint**: `GET /api/payments/fees?amount={amount}&paymentMethod={paymentMethod}`
- **Description**: Calculate fees for a payment method
- **Authentication**: Not required
- **Parameters**:
  - `amount`: Payment amount
  - `paymentMethod`: Payment method
- **Response**: Calculated fees

---

## 🔐 **4. Enhanced Authentication & Security**

### **4.1 Secure Session Management**
- **JWT Token-based authentication**
- **Session timeout configuration**
- **Secure password hashing with BCrypt**
- **Role-based access control**

### **4.2 Security Features**
- **CSRF protection disabled for API endpoints**
- **CORS enabled for cross-origin requests**
- **Input validation and sanitization**
- **SQL injection prevention with JPA**

---

## 📊 **5. Data Models**

### **5.1 Cart Entity**
```java
- id: String (UUID)
- userId: String
- items: List<CartItem>
- totalAmount: BigDecimal
- itemCount: Integer
- status: CartStatus (ACTIVE, CONVERTED, EXPIRED)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### **5.2 CartItem Entity**
```java
- id: String (UUID)
- cart: Cart
- productId: String
- productName: String
- productImage: String
- quantity: Integer
- unitPrice: BigDecimal
- subtotal: BigDecimal
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### **5.3 Order Entity**
```java
- id: String (UUID)
- orderNumber: String (unique)
- userId: String
- items: List<OrderItem>
- subtotal: BigDecimal
- taxAmount: BigDecimal
- shippingAmount: BigDecimal
- totalAmount: BigDecimal
- status: OrderStatus
- paymentStatus: PaymentStatus
- paymentMethod: PaymentMethod
- deliveryAddress: String
- trackingNumber: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### **5.4 Payment Entity**
```java
- id: String (UUID)
- orderId: String
- userId: String
- amount: BigDecimal
- paymentMethod: PaymentMethod
- status: PaymentStatus
- transactionId: String
- cardLastFour: String
- cardBrand: String
- billingAddress: String
- processedAt: LocalDateTime
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

---

## 🚀 **6. Usage Examples**

### **6.1 Complete Shopping Flow**
1. **Add products to cart**: `POST /api/cart/add`
2. **View cart**: `GET /api/cart`
3. **Apply discount**: `POST /api/cart/discount`
4. **Checkout**: `POST /api/cart/checkout`
5. **Process payment**: `POST /api/payments/process`
6. **Track order**: `GET /api/orders/track/{trackingNumber}`

### **6.2 Order Management Flow**
1. **View order history**: `GET /api/orders/user/{userId}`
2. **Get order details**: `GET /api/orders/{orderId}`
3. **Track order**: `GET /api/orders/{orderId}/tracking`
4. **Request refund**: `POST /api/orders/{orderId}/refund`

### **6.3 Payment Management Flow**
1. **View payment history**: `GET /api/payments/user/{userId}`
2. **Get payment details**: `GET /api/payments/{paymentId}`
3. **Process refund**: `POST /api/payments/{paymentId}/refund`
4. **Get receipt**: `GET /api/payments/{paymentId}/receipt`

---

## 📝 **7. Response Format**

All API responses follow a consistent format:

```json
{
  "success": true/false,
  "message": "Response message",
  "data": {
    // Response data
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### **7.1 Success Response Example**
```json
{
  "success": true,
  "message": "Product added to cart successfully",
  "data": {
    "cartId": "uuid",
    "itemCount": 3,
    "totalAmount": 299.99
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### **7.2 Error Response Example**
```json
{
  "success": false,
  "message": "Product not found",
  "error": "PRODUCT_NOT_FOUND",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

---

## 🔧 **8. Configuration & Setup**

### **8.1 Required Dependencies**
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- H2 Database (for development)
- Spring Boot DevTools

### **8.2 Database Configuration**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

### **8.3 Security Configuration**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Security configuration for cart, orders, and payments
}
```

---

## 📚 **9. Testing**

### **9.1 Test Endpoints**
- Use H2 Console: `http://localhost:8080/h2-console`
- Test with Postman or similar API testing tools
- Use provided cURL examples for testing

### **9.2 Sample Test Data**
```sql
-- Insert test products
INSERT INTO products (id, name, price, stock_quantity) 
VALUES ('prod-1', 'Test Product', 99.99, 100);

-- Insert test user
INSERT INTO users (id, email, password, first_name, last_name) 
VALUES ('user-1', 'test@example.com', 'hashed_password', 'John', 'Doe');
```

---

## 🎯 **10. Next Steps**

### **10.1 Implementation Priority**
1. ✅ **Core Entities & Repositories** - Complete
2. ✅ **Service Interfaces** - Complete
3. ✅ **Controllers** - Complete
4. 🔄 **Service Implementations** - In Progress
5. ⏳ **Integration Testing** - Pending
6. ⏳ **Frontend Integration** - Pending

### **10.2 Future Enhancements**
- **Real-time notifications** using WebSockets
- **Email service integration** for confirmations
- **Payment gateway integration** (Stripe, PayPal)
- **Inventory management system**
- **Analytics and reporting dashboard**
- **Mobile app API endpoints**

---

## 📞 **11. Support & Contact**

For questions or issues with the API:
- **Documentation**: This file
- **Code Repository**: Main project directory
- **API Base URL**: `http://localhost:8080/api/`
- **H2 Console**: `http://localhost:8080/h2-console`

---

*This API documentation covers the complete Cart & Checkout, Order Management, and Payment systems for the ecommerce platform.*
