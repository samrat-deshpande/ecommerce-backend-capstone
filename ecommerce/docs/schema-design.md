# 🗄️ Ecommerce Backend Database Schema Design

## 📋 **Complete Database Schema Description**

### **Tables:**

#### **Users**
- `user_id` (Primary Key)
- `username`
- `email`
- `password_hash`
- `first_name`
- `last_name`
- `phone_number`
- `address`
- `city`
- `state`
- `zip_code`
- `country`
- `is_active`
- `created_at`
- `updated_at`

#### **Roles**
- `role_id` (Primary Key)
- `role_name`
- `description`
- `created_at`

#### **User_Roles**
- `user_id`
- `role_id`
- Primary Key(`user_id`, `role_id`)

#### **Categories**
- `category_id` (Primary Key)
- `name`
- `description`
- `image_url`
- `is_active`
- `created_at`
- `updated_at`

#### **Products**
- `product_id` (Primary Key)
- `name`
- `description`
- `price`
- `category` (String - references category name)
- `stock_quantity`
- `image_url`
- `is_active`
- `created_at`
- `updated_at`

#### **Carts**
- `cart_id` (Primary Key)
- `user_id`
- `status` (ACTIVE, CONVERTED)
- `created_at`
- `updated_at`

#### **Cart_Items**
- `cart_item_id` (Primary Key)
- `cart_id`
- `product_id`
- `quantity`
- `price_at_time`
- `created_at`
- `updated_at`

#### **Orders**
- `order_id` (Primary Key)
- `user_id`
- `order_number`
- `order_date`
- `status` (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- `subtotal`
- `tax_amount`
- `shipping_amount`
- `total_amount`
- `shipping_address`
- `billing_address`
- `payment_status` (PENDING, PAID, FAILED, REFUNDED)
- `created_at`
- `updated_at`

#### **Order_Items**
- `order_item_id` (Primary Key)
- `order_id`
- `product_id`
- `quantity`
- `unit_price`
- `total_price`
- `created_at`

#### **Payments**
- `payment_id` (Primary Key)
- `order_id`
- `user_id`
- `amount`
- `currency`
- `payment_method` (CREDIT_CARD, DEBIT_CARD, PAYPAL, STRIPE)
- `transaction_id`
- `status` (PENDING, COMPLETED, FAILED, REFUNDED)
- `gateway_response`
- `created_at`
- `updated_at`

#### **Payment_Transactions**
- `transaction_id` (Primary Key)
- `payment_id`
- `gateway_transaction_id`
- `amount`
- `currency`
- `status`
- `gateway_response_code`
- `gateway_response_message`
- `created_at`

#### **User_Addresses**
- `address_id` (Primary Key)
- `user_id`
- `address_type` (SHIPPING, BILLING)
- `street_address`
- `city`
- `state`
- `zip_code`
- `country`
- `is_default`
- `created_at`
- `updated_at`

#### **Product_Images**
- `image_id` (Primary Key)
- `product_id`
- `image_url`
- `alt_text`
- `is_primary`
- `sort_order`
- `created_at`

#### **Order_Status_History**
- `history_id` (Primary Key)
- `order_id`
- `status`
- `status_date`
- `notes`
- `created_at`

#### **Payment_Status_History**
- `history_id` (Primary Key)
- `payment_id`
- `status`
- `status_date`
- `notes`
- `created_at`

#### **User_Sessions**
- `session_id` (Primary Key)
- `user_id`
- `token`
- `expires_at`
- `created_at`

#### **Password_Reset_Tokens**
- `token_id` (Primary Key)
- `user_id`
- `token`
- `expires_at`
- `used`
- `created_at`

### **Foreign Keys:**

- `Users(user_id)` → `User_Roles(user_id)`
- `Users(user_id)` → `Carts(user_id)`
- `Users(user_id)` → `Orders(user_id)`
- `Users(user_id)` → `Payments(user_id)`
- `Users(user_id)` → `User_Addresses(user_id)`
- `Users(user_id)` → `User_Sessions(user_id)`
- `Users(user_id)` → `Password_Reset_Tokens(user_id)`

- `Roles(role_id)` → `User_Roles(role_id)`

- `Categories(category_id)` → `Products(category)` (String reference)

- `Products(product_id)` → `Cart_Items(product_id)`
- `Products(product_id)` → `Order_Items(product_id)`
- `Products(product_id)` → `Product_Images(product_id)`

- `Carts(cart_id)` → `Cart_Items(cart_id)`

- `Orders(order_id)` → `Order_Items(order_id)`
- `Orders(order_id)` → `Payments(order_id)`
- `Orders(order_id)` → `Order_Status_History(order_id)`

- `Payments(payment_id)` → `Payment_Transactions(payment_id)`
- `Payments(payment_id)` → `Payment_Status_History(payment_id)`

### **Cardinality of Relations:**

#### **One-to-Many (1:M)**
- **Users to Carts**: One user can have multiple carts (but typically one active)
- **Users to Orders**: One user can have multiple orders
- **Users to Payments**: One user can have multiple payments
- **Users to Addresses**: One user can have multiple addresses
- **Categories to Products**: One category can have multiple products
- **Products to Cart Items**: One product can be in multiple cart items
- **Products to Order Items**: One product can be in multiple order items
- **Products to Images**: One product can have multiple images
- **Carts to Cart Items**: One cart can have multiple cart items
- **Orders to Order Items**: One order can have multiple order items
- **Orders to Payments**: One order can have multiple payment attempts
- **Payments to Transactions**: One payment can have multiple transactions
- **Orders to Status History**: One order can have multiple status changes
- **Payments to Status History**: One payment can have multiple status changes

#### **Many-to-Many (M:M)**
- **Users to Roles**: Users can have multiple roles, roles can be assigned to multiple users
- **Orders to Products**: Orders can contain multiple products, products can be in multiple orders

#### **One-to-One (1:1)**
- **Orders to Payments**: One order typically has one successful payment
- **Users to Active Cart**: One user typically has one active cart at a time

### **Key Design Patterns:**

#### **Audit Trail**
- All major entities include `created_at` and `updated_at` timestamps
- Status history tables track changes over time for orders and payments

#### **Soft Deletion**
- Entities use `is_active` flags instead of hard deletion
- Maintains referential integrity and audit trail

#### **Flexible Address Management**
- Separate address table allows users to have multiple addresses
- Address type distinction between shipping and billing

#### **Payment Gateway Integration**
- Payment transactions table stores gateway-specific responses
- Supports multiple payment methods and gateways

#### **Event-Driven Architecture Support**
- Status history tables enable event tracking
- Supports Kafka event publishing for status changes

### **Indexing Strategy:**

#### **Primary Indexes**
- All primary keys are automatically indexed
- Foreign key columns for performance

#### **Secondary Indexes**
- `Users(email)` - Unique constraint for login
- `Users(username)` - Unique constraint for login
- `Products(category)` - For category-based queries
- `Orders(user_id, order_date)` - For user order history
- `Payments(order_id, status)` - For payment status queries
- `Carts(user_id, status)` - For active cart queries

### **Data Types and Constraints:**

#### **String Fields**
- Email addresses: VARCHAR(255) with email validation
- Phone numbers: VARCHAR(20) for international support
- Addresses: TEXT for flexibility
- Product names: VARCHAR(255)
- Category names: VARCHAR(100)

#### **Numeric Fields**
- Prices: DECIMAL(10,2) for currency precision
- Quantities: INTEGER with positive constraints
- IDs: BIGINT for scalability

#### **Date Fields**
- All timestamps: TIMESTAMP with timezone support
- Expiration dates: TIMESTAMP for token management

#### **Boolean Fields**
- Status flags: BOOLEAN for simple true/false values
- Active flags: BOOLEAN for soft deletion

### **Security Considerations:**

#### **Password Security**
- Passwords stored as BCrypt hashes
- No plain text password storage

#### **Session Management**
- JWT tokens with expiration
- Secure token generation and validation

#### **Data Access Control**
- Role-based access control (RBAC)
- User data isolation

### **Scalability Features:**

#### **Horizontal Scaling**
- Stateless design supports multiple application instances
- Database connection pooling
- Read replicas for read-heavy operations

#### **Performance Optimization**
- Efficient indexing strategy
- Pagination support for large datasets
- Caching opportunities for product catalog

#### **Event Streaming**
- Kafka integration for asynchronous processing
- Decoupled service communication
- Scalable message handling

---

*This schema design provides a robust foundation for an enterprise-grade ecommerce system with support for user management, product catalog, shopping cart, order processing, payment integration, and comprehensive audit trails.*
