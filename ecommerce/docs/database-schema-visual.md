# 🎨 Visual Database Schema Diagram

## 📊 **Database Tables Structure**

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                    USERS                                          │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ email (UK) │ password │ firstName │ lastName │ phoneNumber │ role     │
│  emailVerified │ phoneVerified │ active │ createdAt │ updatedAt                    │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              PASSWORD_RESET_TOKENS                                 │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ userId (FK) │ token │ expiryDate │ createdAt                           │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                  CATEGORIES                                       │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ name │ description │ parentId (FK) │ featured │ active │ sortOrder    │
│  productCount │ createdAt │ updatedAt                                              │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N (self-referencing)
                                    │
                                    │ 1:N
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                   PRODUCTS                                        │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ name │ description │ price │ originalPrice │ discountPercentage       │
│  category │ subCategory │ brand │ model │ sku │ stockQuantity │ minStockLevel    │
│  reviewCount │ averageRating │ weight │ dimensions │ imageUrl │ featured │ active │
│  createdAt │ updatedAt                                                              │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                PRODUCT_IMAGES                                     │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ productId (FK) │ imageUrl │ altText │ sortOrder │ primary │ createdAt  │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                    CARTS                                         │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ userId (FK) │ status │ subtotal │ taxAmount │ totalAmount │ discountCode│
│  discountAmount │ createdAt │ updatedAt                                            │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                CART_ITEMS                                        │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ cartId (FK) │ productId (FK) │ quantity │ unitPrice │ totalPrice     │
│  createdAt                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                   ORDERS                                         │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ userId (FK) │ orderNumber (UK) │ status │ paymentStatus │ subtotal    │
│  taxAmount │ shippingAmount │ totalAmount │ deliveryAddress │ paymentMethod     │
│  shippingMethod │ notes │ orderDate │ createdAt │ updatedAt                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ 1:N
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                ORDER_ITEMS                                       │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ orderId (FK) │ productId (FK) │ quantity │ unitPrice │ totalPrice     │
│  createdAt                                                                         │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                  PAYMENTS                                        │
├─────────────────────────────────────────────────────────────────────────────────────┤
│  id (PK) │ orderId (FK) │ userId (FK) │ transactionId (UK) │ status │ paymentMethod│
│  amount │ currency │ gatewayResponse │ paymentDate │ createdAt │ updatedAt        │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

## 🔗 **Relationship Mapping**

### **One-to-Many Relationships**
```
USERS (1) ──────→ (N) CARTS
USERS (1) ──────→ (N) ORDERS  
USERS (1) ──────→ (N) PAYMENTS
USERS (1) ──────→ (N) PASSWORD_RESET_TOKENS

CATEGORIES (1) ──→ (N) PRODUCTS
CATEGORIES (1) ──→ (N) CATEGORIES (self-referencing)

PRODUCTS (1) ────→ (N) PRODUCT_IMAGES
PRODUCTS (1) ────→ (N) CART_ITEMS
PRODUCTS (1) ────→ (N) ORDER_ITEMS

CARTS (1) ──────→ (N) CART_ITEMS
ORDERS (1) ─────→ (N) ORDER_ITEMS
ORDERS (1) ─────→ (N) PAYMENTS
```

### **Many-to-One Relationships**
```
CART_ITEMS (N) ──→ (1) PRODUCTS
CART_ITEMS (N) ──→ (1) CARTS
ORDER_ITEMS (N) ─→ (1) PRODUCTS
ORDER_ITEMS (N) ─→ (1) ORDERS
PAYMENTS (N) ────→ (1) ORDERS
PAYMENTS (N) ────→ (1) USERS
```

## 📋 **Table Summary**

| Table | Primary Key | Foreign Keys | Unique Keys | Records |
|-------|-------------|--------------|-------------|---------|
| **USERS** | `id` | - | `email` | 7 |
| **CATEGORIES** | `id` | `parentId` | - | 8 |
| **PRODUCTS** | `id` | - | `sku` | 6 |
| **PRODUCT_IMAGES** | `id` | `productId` | - | Dynamic |
| **CARTS** | `id` | `userId` | - | Dynamic |
| **CART_ITEMS** | `id` | `cartId`, `productId` | - | Dynamic |
| **ORDERS** | `id` | `userId` | `orderNumber` | Dynamic |
| **ORDER_ITEMS** | `id` | `orderId`, `productId` | - | Dynamic |
| **PAYMENTS** | `id` | `orderId`, `userId` | `transactionId` | Dynamic |
| **PASSWORD_RESET_TOKENS** | `id` | `userId` | - | Dynamic |

## 🎯 **Key Design Patterns**

### **1. Audit Trail Pattern**
- All tables include `createdAt` and `updatedAt` timestamps
- Tracks data creation and modification history

### **2. Soft Delete Pattern**
- `active` boolean flags instead of hard deletes
- Maintains data integrity and history

### **3. Hierarchical Data Pattern**
- Categories support parent-child relationships
- Self-referencing foreign key for tree structure

### **4. Junction Table Pattern**
- `CART_ITEMS` and `ORDER_ITEMS` as junction tables
- Many-to-many relationships between entities

### **5. Status Enum Pattern**
- Order status, payment status, cart status
- Predefined state management

---

**Visual Schema Status: ✅ COMPLETE**

This visual representation clearly shows the database structure, relationships, and design patterns used in your ecommerce backend.
