# User Management System

## Overview

The User Management System is a comprehensive solution that handles user registration, login, profile management, and password reset functionality. It uses MySQL as the primary database and integrates with Kafka for event-driven communication with other services.

## Features

### Core Functionality
- **User Registration**: Secure user account creation with validation
- **User Authentication**: JWT-based login system with password encryption
- **Profile Management**: Update user information and preferences
- **Password Reset**: Secure password reset via email tokens
- **Account Deletion**: Permanent account removal with data cleanup

### Security Features
- **Password Encryption**: BCrypt hashing for secure password storage
- **JWT Tokens**: Secure authentication tokens with configurable expiration
- **Input Validation**: Comprehensive validation using Bean Validation
- **Role-Based Access**: Support for USER, ADMIN, and MODERATOR roles

### Event-Driven Architecture
- **Kafka Integration**: Real-time event publishing for user activities
- **Event Types**: User registration, login, profile updates, and more
- **Service Communication**: Enables other services to react to user events

## Architecture

### Database Layer
- **MySQL Database**: Primary data storage with JPA/Hibernate
- **Entities**: User, PasswordResetToken, and related entities
- **Repositories**: Spring Data JPA repositories for data access

### Service Layer
- **UserService**: Core business logic for user operations
- **JwtService**: JWT token generation and validation
- **KafkaProducerService**: Event publishing to Kafka topics

### Controller Layer
- **UserController**: RESTful API endpoints with Swagger documentation
- **Validation**: Request validation using DTOs and Bean Validation

### Event System
- **UserEvent**: Base class for all user-related events
- **UserRegistrationEvent**: Events for new user registrations
- **UserLoginEvent**: Events for user login activities

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role ENUM('USER', 'ADMIN', 'MODERATOR') NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);
```

### Password Reset Tokens Table
```sql
CREATE TABLE password_reset_tokens (
    id VARCHAR(36) PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## API Endpoints

### Authentication
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User authentication
- `POST /api/users/password/reset-request` - Request password reset
- `POST /api/users/password/reset` - Reset password with token
- `POST /api/users/password/change/{userId}` - Change password

### Profile Management
- `GET /api/users/profile/{userId}` - Get user profile
- `PUT /api/users/profile/{userId}` - Update user profile
- `DELETE /api/users/profile/{userId}` - Delete user account

### Utilities
- `GET /api/users/check-email` - Check email availability
- `GET /api/users/stats/{userId}` - Get user statistics
- `POST /api/users/verify-email` - Verify email address
- `POST /api/users/resend-verification` - Resend email verification

## Configuration

### Application Properties
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Kafka Topics
- `user-events` - General user activity events
- `user-registrations` - New user registration events
- `user-logins` - User login events

## Event Structure

### User Registration Event
```json
{
  "eventId": "uuid",
  "eventType": "USER_REGISTERED",
  "userId": "user-uuid",
  "userEmail": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1-555-0101",
  "role": "USER",
  "timestamp": "2024-01-20T10:30:00",
  "source": "ecommerce-user-service"
}
```

### User Login Event
```json
{
  "eventId": "uuid",
  "eventType": "USER_LOGIN",
  "userId": "user-uuid",
  "userEmail": "user@example.com",
  "loginMethod": "EMAIL_PASSWORD",
  "userAgent": "Web Client",
  "ipAddress": "127.0.0.1",
  "successful": true,
  "timestamp": "2024-01-20T10:30:00",
  "source": "ecommerce-user-service"
}
```

## Dependencies

### Required Dependencies
```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- Spring Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

## Setup Instructions

### 1. Database Setup
```sql
-- Create database
CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use database
USE ecommerce;

-- Run the data-mysql.sql script to populate sample data
source /path/to/data-mysql.sql;
```

### 2. Kafka Setup
```bash
# Start Kafka (using Docker)
docker run -p 9092:9092 apache/kafka:2.13-3.6.1

# Create topics
kafka-topics.sh --create --topic user-events --bootstrap-server localhost:9092 --partitions 3 --replicas 1
kafka-topics.sh --create --topic user-registrations --bootstrap-server localhost:9092 --partitions 3 --replicas 1
kafka-topics.sh --create --topic user-logins --bootstrap-server localhost:9092 --partitions 3 --replicas 1
```

### 3. Application Configuration
1. Update `application.properties` with your database and Kafka settings
2. Configure email settings for password reset functionality
3. Set a secure JWT secret key

### 4. Run Application
```bash
./gradlew bootRun
```

## Testing

### Sample API Calls

#### User Registration
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "+1-555-0123"
  }'
```

#### User Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Get User Profile
```bash
curl -X GET http://localhost:8080/api/users/profile/user-id-here \
  -H "Authorization: Bearer your-jwt-token"
```

## Security Considerations

### Password Security
- Passwords are hashed using BCrypt with salt
- Minimum password length enforced
- Password reset tokens have expiration

### JWT Security
- Tokens include user ID and role claims
- Configurable expiration time
- Secure secret key required

### Input Validation
- All inputs validated using Bean Validation
- SQL injection prevention through JPA
- XSS protection through proper encoding

## Monitoring and Logging

### Logging
- Comprehensive logging for all user operations
- Error logging with stack traces
- Audit logging for security events

### Kafka Monitoring
- Event publishing success/failure logging
- Topic monitoring for event flow
- Consumer group monitoring

## Troubleshooting

### Common Issues

#### Database Connection
- Verify MySQL service is running
- Check database credentials in application.properties
- Ensure database exists and is accessible

#### Kafka Connection
- Verify Kafka service is running on configured port
- Check topic creation and permissions
- Monitor Kafka logs for connection issues

#### JWT Issues
- Verify JWT secret is properly configured
- Check token expiration settings
- Ensure proper token format in requests

### Debug Mode
Enable debug logging in application.properties:
```properties
logging.level.com.backend.ecommerce=DEBUG
logging.level.org.springframework.kafka=DEBUG
```

## Future Enhancements

### Planned Features
- **Email Verification**: Complete email verification workflow
- **Two-Factor Authentication**: SMS or app-based 2FA
- **Social Login**: OAuth integration with Google, Facebook, etc.
- **User Analytics**: Advanced user behavior tracking
- **Audit Trail**: Comprehensive audit logging system

### Scalability Improvements
- **Redis Caching**: User session and profile caching
- **Database Sharding**: Horizontal database scaling
- **Load Balancing**: Multiple application instances
- **Event Sourcing**: Complete event history tracking

## Support

For technical support or questions about the User Management System, please refer to the project documentation or contact the development team.

## License

This project is licensed under the MIT License. See LICENSE file for details.
