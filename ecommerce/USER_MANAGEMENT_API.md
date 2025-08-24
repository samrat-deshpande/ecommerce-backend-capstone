# User Management API Documentation

This document describes the User Management API endpoints for the Ecommerce Backend application.

## Base URL
```
http://localhost:8080/api/users
```

## Authentication
Most endpoints require authentication. The following endpoints are public:
- `POST /register` - User registration
- `POST /login` - User authentication
- `POST /forgot-password` - Request password reset
- `POST /reset-password` - Reset password with token
- `GET /check-email` - Check if email exists

## Endpoints

### 1. User Registration
**POST** `/register`

Allows new users to create an account.

**Request Body:**
```json
{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "User registered successfully",
    "userId": "generated-uuid",
    "email": "user@example.com"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "Email is required"
}
```

**Validation Rules:**
- Email: Required, must be unique
- Password: Required, minimum 6 characters
- First Name: Required, non-empty
- Last Name: Required, non-empty
- Phone Number: Optional

### 2. User Login
**POST** `/login`

Authenticates user and returns access token.

**Request Body:**
```json
{
    "email": "user@example.com",
    "password": "password123"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Login successful",
    "token": "generated-uuid-token",
    "user": {
        "id": "user-uuid",
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "role": "USER",
        "emailVerified": false,
        "phoneVerified": false
    }
}
```

**Response (Error - 401):**
```json
{
    "success": false,
    "message": "Invalid email or password"
}
```

### 3. Get User Profile
**GET** `/{userId}`

Retrieves user profile information.

**Headers:**
```
Authorization: Bearer {token}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "user": {
        "id": "user-uuid",
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "phoneNumber": "+1234567890",
        "role": "USER",
        "active": true,
        "emailVerified": false,
        "phoneVerified": false,
        "createdAt": "2025-08-22T21:00:00Z",
        "updatedAt": "2025-08-22T21:00:00Z",
        "lastLogin": "2025-08-22T21:00:00Z"
    }
}
```

**Response (Error - 404):**
```json
{
    "success": false,
    "message": "User not found"
}
```

### 4. Update User Profile
**PUT** `/{userId}`

Updates user profile information.

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
    "firstName": "Jane",
    "lastName": "Smith",
    "phoneNumber": "+1987654321"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "User profile updated successfully"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "User not found"
}
```

### 5. Delete User Account
**DELETE** `/{userId}`

Soft deletes user account (marks as inactive).

**Headers:**
```
Authorization: Bearer {token}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "User account deleted successfully"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "User not found"
}
```

### 6. Request Password Reset
**POST** `/forgot-password`

Requests a password reset link to be sent to user's email.

**Request Body:**
```json
{
    "email": "user@example.com"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Password reset link has been sent to your email"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "Email is required"
}
```

### 7. Reset Password
**POST** `/reset-password`

Resets password using the reset token.

**Request Body:**
```json
{
    "resetToken": "reset-token-uuid",
    "newPassword": "newpassword123"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Password has been reset successfully"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "Reset token and new password are required"
}
```

### 8. Change Password
**POST** `/{userId}/change-password`

Changes password for authenticated user.

**Headers:**
```
Authorization: Bearer {token}
```

**Request Body:**
```json
{
    "currentPassword": "oldpassword123",
    "newPassword": "newpassword123"
}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "message": "Password changed successfully"
}
```

**Response (Error - 400):**
```json
{
    "success": false,
    "message": "Current password is incorrect"
}
```

### 9. Check Email Exists
**GET** `/check-email?email={email}`

Checks if an email address is already registered.

**Response (Success - 200):**
```json
{
    "success": true,
    "emailExists": true,
    "message": "Email already exists"
}
```

### 10. Get User Statistics
**GET** `/stats`

Retrieves user statistics (for admin use).

**Headers:**
```
Authorization: Bearer {token}
```

**Response (Success - 200):**
```json
{
    "success": true,
    "stats": {
        "totalUsers": 150,
        "activeUsers": 120,
        "newUsersToday": 5,
        "verifiedUsers": 80
    }
}
```

## Error Handling

All endpoints return consistent error responses with the following structure:

```json
{
    "success": false,
    "message": "Error description"
}
```

## HTTP Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Security Features

1. **Password Hashing**: All passwords are hashed using BCrypt
2. **Input Validation**: Comprehensive validation of all input data
3. **Soft Delete**: User accounts are marked as inactive rather than permanently deleted
4. **Token-based Authentication**: Secure token generation for authenticated sessions
5. **CORS Support**: Cross-origin requests are supported for frontend integration

## Development Notes

- The application uses H2 in-memory database for development
- H2 console is available at `/h2-console`
- Password reset tokens are currently generated but not stored (TODO: implement token storage)
- Email functionality is not implemented (TODO: integrate email service)
- JWT tokens are not implemented (TODO: replace UUID tokens with JWT)

## Testing the API

You can test the API using tools like:
- Postman
- cURL
- Insomnia
- Any HTTP client

### Example cURL Commands

**Register a new user:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Get user profile (replace {token} and {userId}):**
```bash
curl -X GET http://localhost:8080/api/users/{userId} \
  -H "Authorization: Bearer {token}"
```
