# 🔑 JWT Token Generation Guide

## 📋 **Overview**

This guide explains how to generate and use JWT (JSON Web Token) tokens for authenticating your ecommerce backend APIs. JWT tokens provide stateless authentication and are essential for securing your REST endpoints.

---

## 🚀 **How to Generate JWT Tokens**

### **1. User Login (Primary Method)**

The main way to get a JWT token is through user login:

```http
POST /api/users/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

**Response:**
```json
{
    "success": true,
    "message": "Login successful",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": "user-uuid",
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "role": "USER"
    }
}
```

### **2. Using the JWT Token**

Once you have the token, include it in the `Authorization` header for subsequent API calls:

```http
GET /api/users/profile/{userId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🔧 **JWT Service Implementation**

### **Core JWT Service Methods**

Your `JwtService` class provides these key methods:

```java
@Service
public class JwtService {
    
    // Generate token for user
    public String generateToken(UserDetails userDetails)
    
    // Generate token with extra claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    
    // Generate token with custom expiration
    public String generateToken(UserDetails userDetails, long customExpiration)
    
    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails)
    
    // Extract username from token
    public String extractUsername(String token)
}
```

### **Token Generation with Claims**

Your login service generates tokens with user-specific claims:

```java
// In UserServiceImpl.loginUser()
Map<String, Object> extraClaims = new HashMap<>();
extraClaims.put("userId", user.getId());
extraClaims.put("role", user.getRole().toString());

String token = jwtService.generateToken(extraClaims, userDetails);
```

---

## ⚙️ **Configuration**

### **JWT Properties**

Configure JWT settings in `application.properties`:

```properties
# JWT Configuration
jwt.secret=your-secret-key-here-make-it-very-long-and-secure-for-production
jwt.expiration=86400000
```

**Key Settings:**
- **`jwt.secret`**: Secret key for signing tokens (keep secure!)
- **`jwt.expiration`**: Token expiration time in milliseconds (24 hours = 86400000ms)

### **Security Configuration**

Your Spring Security configuration should include JWT authentication:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

---

## 📱 **API Usage Examples**

### **1. User Registration & Login Flow**

```bash
# Step 1: Register a new user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }'

# Step 2: Login to get JWT token
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123"
  }'
```

### **2. Using JWT Token for Authenticated Requests**

```bash
# Get user profile (requires authentication)
curl -X GET http://localhost:8080/api/users/profile/{userId} \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"

# Create order (requires authentication)
curl -X POST http://localhost:8080/api/orders/create \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "cartId": "123",
    "userId": "456"
  }'
```

---

## 🔒 **Security Best Practices**

### **1. Token Storage**

**Frontend (Browser):**
```javascript
// Store token in localStorage or sessionStorage
localStorage.setItem('jwt_token', response.token);

// Include in API calls
const token = localStorage.getItem('jwt_token');
fetch('/api/users/profile', {
    headers: {
        'Authorization': `Bearer ${token}`
    }
});
```

**Mobile Apps:**
```java
// Store token securely in Android Keystore or iOS Keychain
SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
prefs.edit().putString("jwt_token", token).apply();
```

### **2. Token Validation**

Your service automatically validates tokens:

```java
// Check if token is valid
if (jwtService.validateToken(token, userDetails)) {
    // Token is valid, proceed with request
    String username = jwtService.extractUsername(token);
    String userId = jwtService.extractUserId(token);
    String role = jwtService.extractUserRole(token);
} else {
    // Token is invalid or expired
    throw new UnauthorizedException("Invalid token");
}
```

---

## 🚨 **Common Issues & Solutions**

### **1. Token Expired**

**Error:** `401 Unauthorized` with "Token expired" message

**Solution:** User needs to login again to get a new token

```java
// Check token expiration
if (jwtService.isTokenExpired(token)) {
    // Token expired, redirect to login
    return ResponseEntity.status(401).body("Token expired");
}
```

### **2. Invalid Token Format**

**Error:** `400 Bad Request` with "Invalid token format"

**Solution:** Ensure token is properly formatted in Authorization header

```http
# Correct format
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# Wrong format (missing "Bearer ")
Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### **3. Missing Token**

**Error:** `401 Unauthorized` with "Missing authentication token"

**Solution:** Include JWT token in request headers

```javascript
// Always include token in authenticated requests
const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
};
```

---

## 🔄 **Token Refresh Strategy**

### **Current Implementation**

Your current setup generates tokens with a 24-hour expiration. For better user experience, consider implementing:

### **Refresh Token Approach**

```java
// Generate access token (short-lived: 15 minutes)
String accessToken = jwtService.generateToken(userDetails, 15 * 60 * 1000);

// Generate refresh token (long-lived: 7 days)
String refreshToken = jwtService.generateToken(userDetails, 7 * 24 * 60 * 60 * 1000);

// Store refresh token securely
user.setRefreshToken(refreshToken);
userRepository.save(user);
```

### **Refresh Endpoint**

```java
@PostMapping("/refresh")
public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();
    
    if (jwtService.validateToken(refreshToken, userDetails)) {
        // Generate new access token
        String newAccessToken = jwtService.generateToken(userDetails);
        
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("tokenType", "Bearer");
        
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(401).body("Invalid refresh token");
    }
}
```

---

## 📊 **Testing JWT Tokens**

### **1. Using Postman**

1. **Login Request:**
   - Method: `POST`
   - URL: `http://localhost:8080/api/users/login`
   - Body: JSON with email and password

2. **Copy Token:**
   - From response, copy the `token` value

3. **Use Token:**
   - In subsequent requests, add header:
   - `Authorization: Bearer YOUR_TOKEN_HERE`

### **2. Using cURL**

```bash
# Login and save token
TOKEN=$(curl -s -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}' \
  | jq -r '.token')

# Use token in authenticated request
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer $TOKEN"
```

### **3. Using JavaScript/Frontend**

```javascript
// Login function
async function login(email, password) {
    const response = await fetch('/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    });
    
    const data = await response.json();
    
    if (data.success) {
        // Store token
        localStorage.setItem('jwt_token', data.token);
        return data.token;
    } else {
        throw new Error(data.error);
    }
}

// Authenticated request function
async function authenticatedRequest(url, options = {}) {
    const token = localStorage.getItem('jwt_token');
    
    const headers = {
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };
    
    const response = await fetch(url, {
        ...options,
        headers
    });
    
    if (response.status === 401) {
        // Token expired, redirect to login
        localStorage.removeItem('jwt_token');
        window.location.href = '/login';
        return;
    }
    
    return response;
}
```

---

## 📝 **Summary**

### **Key Points:**

1. **JWT tokens are generated during user login** through `/api/users/login`
2. **Include token in Authorization header** as `Bearer {token}` for authenticated requests
3. **Tokens expire after 24 hours** (configurable in `application.properties`)
4. **Store tokens securely** in frontend applications
5. **Handle token expiration** gracefully with proper error handling

### **Security Features:**

- **Stateless authentication** - no server-side session storage
- **Digital signatures** prevent token tampering
- **Configurable expiration** for security
- **Role-based claims** for authorization
- **Secure storage** recommendations for different platforms

---

*JWT tokens provide secure, stateless authentication for your ecommerce backend APIs, enabling scalable and secure user management.*
