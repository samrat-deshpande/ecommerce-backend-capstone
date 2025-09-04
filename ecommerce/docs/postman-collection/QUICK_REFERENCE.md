# 🚀 Quick Reference Guide - Ecommerce API Collection

## 📋 Collection Overview
- **Total Endpoints**: 40+ API endpoints
- **Authentication**: JWT Bearer Token
- **Base URL**: `http://localhost:8080`
- **Format**: JSON

## 🔑 Quick Start

### 1. Import Collection
```
File → Import → Select Ecommerce-API-Collection.postman_collection.json
```

### 2. Set Environment Variables
```
baseUrl: http://localhost:8080
authToken: (auto-populated after login)
userId: (auto-populated after registration)
```

### 3. Test Basic Flow
```
Health Check → Register User → Login → Browse Products
```

## 📱 Essential Endpoints

### 🔓 Public Endpoints (No Auth Required)
- `GET /` - Welcome message
- `GET /health` - Health check
- `GET /api/test/**` - Test endpoints
- `GET /api/catalog/**` - Product browsing
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User login

### 🔒 Protected Endpoints (Auth Required)
- `GET /api/users/profile/**` - User profile
- `GET /api/cart/**` - Shopping cart
- `POST /api/orders` - Create order
- `POST /api/payments/**` - Payment processing

## 🧪 Testing Workflows

### User Journey Testing
```
1. Register User
2. Login → Get Token
3. Browse Products
4. Add to Cart
5. Create Order
6. Process Payment
```

### Admin Testing
```
1. Login as Admin
2. View All Orders
3. Update Order Status
4. Process Refunds
```

## 📊 Response Codes

| Code | Meaning | Action |
|------|---------|---------|
| 200 | Success | Continue |
| 201 | Created | Check response body |
| 400 | Bad Request | Check request data |
| 401 | Unauthorized | Login first |
| 404 | Not Found | Check URL/ID |
| 500 | Server Error | Check logs |

## 🔧 Common Headers

### Request Headers
```
Content-Type: application/json
Authorization: Bearer {{authToken}}
Accept: application/json
```

### Response Headers
```
Content-Type: application/json
X-Request-ID: [unique-id]
```

## 📝 Sample Request Bodies

### User Registration
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1-555-0100"
}
```

### Add to Cart
```json
{
  "productId": "prod-001",
  "quantity": 2,
  "userId": "{{userId}}"
}
```

### Create Order
```json
{
  "deliveryAddress": "123 Main St, City, State 12345",
  "paymentMethod": "CREDIT_CARD",
  "shippingMethod": "STANDARD"
}
```

## 🚨 Troubleshooting

### Authentication Issues
- ✅ Run Login request first
- ✅ Check `authToken` variable is set
- ✅ Verify token format: `Bearer <token>`

### Request Errors
- ✅ Check JSON syntax
- ✅ Verify required fields
- ✅ Check data types

### Server Errors
- ✅ Verify application is running
- ✅ Check database connectivity
- ✅ Verify Kafka is running

## 📚 Useful URLs

| Service | URL | Description |
|---------|-----|-------------|
| **API Base** | `http://localhost:8080` | Main API |
| **Swagger UI** | `/swagger-ui/index.html` | API Documentation |
| **H2 Console** | `/h2-console` | Database Access |
| **Actuator** | `/actuator` | Application Monitoring |

## 🎯 Pro Tips

1. **Use Variables**: All dynamic values are auto-stored
2. **Test in Order**: Follow the workflow sequence
3. **Check Responses**: Verify status codes and data
4. **Monitor Logs**: Check application console for errors
5. **Use Collections**: Organize tests by functionality

## 🔄 Environment Switching

### Development
```
baseUrl: http://localhost:8080
```

### Production
```
baseUrl: https://your-domain.com
```

### Testing
```
baseUrl: http://localhost:8081
```

---

**Need Help?** Check the full README.md for detailed instructions! 📖
