# 🚀 Ecommerce API Postman Collection

This Postman collection contains all the API endpoints for the Ecommerce Backend application, organized by functionality with proper authentication, request bodies, and test scenarios.

## 📁 Collection Structure

The collection is organized into the following folders:

### 🏠 Home & Health
- **Home - Welcome**: Get application welcome message and endpoint information
- **Health Check**: Check application health status

### 🧪 Test & Development
- **Test Health**: Test controller health check
- **Test Info**: Get test controller information
- **Test Kafka**: Test Kafka integration

### 👤 User Management
- **Register User**: Create new user account
- **User Login**: Authenticate user and get JWT token
- **Get User Profile**: Retrieve user profile (requires auth)
- **Update User Profile**: Update user information (requires auth)
- **Change Password**: Change user password (requires auth)
- **Forgot Password**: Request password reset email
- **Reset Password**: Reset password using token
- **Check Email Availability**: Verify email availability for registration

### 🛍️ Product Catalog
- **Get All Products**: Browse products with pagination and filtering
- **Get Products by Category**: Filter products by category
- **Search Products**: Search products by name/description
- **Get Product by ID**: Get detailed product information
- **Get All Categories**: Browse all product categories
- **Get Products by Price Range**: Filter products by price

### 🛒 Shopping Cart
- **Get User Cart**: View current shopping cart (requires auth)
- **Add Product to Cart**: Add item to cart (requires auth)
- **Update Cart Item Quantity**: Modify item quantity (requires auth)
- **Remove Item from Cart**: Remove item from cart (requires auth)
- **Clear Cart**: Empty entire cart (requires auth)
- **Apply Discount Code**: Apply discount to cart (requires auth)

### 📦 Order Management
- **Create Order from Cart**: Convert cart to order (requires auth)
- **Get Order by ID**: View order details (requires auth)
- **Get Order by Order Number**: Find order by order number (requires auth)
- **Get User Orders**: View user's order history (requires auth)
- **Cancel Order**: Cancel existing order (requires auth)
- **Update Order Status**: Update order status (Admin only)

### 💳 Payment Processing
- **Process Payment**: Process payment for order (requires auth)
- **Verify Payment**: Verify payment transaction (requires auth)
- **Get Payment Details**: View payment information (requires auth)
- **Get User Payment History**: View payment history (requires auth)
- **Process Refund**: Process refund (requires auth)

### 📊 Monitoring & Health
- **Actuator Health**: Application health via Spring Boot Actuator
- **Actuator Info**: Application information
- **Actuator Metrics**: Application metrics
- **H2 Console**: Database console access (development only)

## 🔧 Setup Instructions

### 1. Import Collection
1. Open Postman
2. Click "Import" button
3. Select the `Ecommerce-API-Collection.postman_collection.json` file
4. The collection will be imported with all endpoints

### 2. Environment Variables
The collection uses the following environment variables:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `baseUrl` | `http://localhost:8080` | Base URL for the API |
| `authToken` | (empty) | JWT authentication token |
| `userId` | (empty) | User ID for authenticated requests |
| `orderId` | (empty) | Order ID for order operations |
| `paymentId` | (empty) | Payment ID for payment operations |

### 3. Authentication Flow
1. **Register User**: Creates a new user account
2. **User Login**: Authenticates and returns JWT token
3. **Token Storage**: The collection automatically stores the token in `authToken` variable
4. **Authenticated Requests**: Use the stored token for protected endpoints

## 🧪 Testing Scenarios

### Basic Flow Testing
1. **Health Check**: Verify application is running
2. **User Registration**: Create a test user
3. **User Login**: Get authentication token
4. **Browse Products**: View available products
5. **Add to Cart**: Add products to shopping cart
6. **Create Order**: Convert cart to order
7. **Process Payment**: Complete the purchase

### Advanced Testing
- **Error Handling**: Test with invalid data
- **Authentication**: Test protected endpoints without token
- **Validation**: Test with missing required fields
- **Edge Cases**: Test boundary conditions

## 📝 Request Examples

### User Registration
```json
{
  "email": "testuser@example.com",
  "password": "SecurePass123!",
  "firstName": "Test",
  "lastName": "User",
  "phoneNumber": "+1-555-0100"
}
```

### Product Search
```
GET {{baseUrl}}/api/catalog/products?page=0&size=20&searchTerm=laptop&minPrice=100&maxPrice=1000
```

### Add to Cart
```json
{
  "productId": "prod-001",
  "quantity": 2,
  "userId": "{{userId}}"
}
```

## 🔒 Security Features

- **JWT Authentication**: Bearer token authentication for protected endpoints
- **Automatic Token Storage**: Tokens are automatically captured and stored
- **Variable Substitution**: Dynamic values are automatically populated
- **Request Validation**: Built-in validation for request bodies

## 🚨 Common Issues & Solutions

### 1. Authentication Errors (401)
- Ensure you've logged in first
- Check that the `authToken` variable is set
- Verify the token hasn't expired

### 2. Validation Errors (400)
- Check required fields in request body
- Verify data types (e.g., numbers vs strings)
- Ensure proper JSON format

### 3. Not Found Errors (404)
- Verify the endpoint URL is correct
- Check that IDs exist in the database
- Ensure the application is running

### 4. Server Errors (500)
- Check application logs
- Verify database connectivity
- Check Kafka service status

## 📊 Response Validation

The collection includes automatic tests that:
- Verify status codes (200, 201, 204)
- Check response time (< 5 seconds)
- Validate response headers
- Store dynamic values automatically

## 🔄 Workflow Examples

### Complete Shopping Flow
1. **Register** → **Login** → **Browse Products** → **Add to Cart** → **Create Order** → **Process Payment**

### User Management Flow
1. **Register** → **Login** → **Update Profile** → **Change Password**

### Admin Operations Flow
1. **Login as Admin** → **View Orders** → **Update Order Status** → **Process Refunds**

## 📚 Additional Resources

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console`
- **Actuator**: `http://localhost:8080/actuator`

## 🆘 Support

If you encounter issues:
1. Check the application logs
2. Verify all services are running (Kafka, Database)
3. Test individual endpoints
4. Check environment variables are set correctly

---

**Happy Testing! 🎯**
