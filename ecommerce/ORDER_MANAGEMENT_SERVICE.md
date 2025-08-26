# Order Management Service

## Overview
The Order Management Service is a comprehensive service that handles order processing, history, and tracking in the ecommerce backend. It communicates with Payment Service and User Management Service through Kafka for real-time order status updates, payment verifications, and user status changes.

## Features

### Core Functionality
- **Order Creation**: Create orders from shopping cart with automatic Kafka event publishing
- **Order Processing**: Handle order lifecycle from creation to delivery
- **Order Tracking**: Provide real-time tracking information for orders
- **Order History**: Retrieve paginated order history for users
- **Status Management**: Update order and payment statuses with event publishing

### Kafka Integration
- **Event Publishing**: Automatically publish events for all order state changes
- **Payment Communication**: Send payment verification requests to Payment Service
- **User Status Updates**: Consume user status changes from User Management Service
- **Real-time Updates**: Enable real-time synchronization between services

## Architecture

### Service Components
1. **OrderManagementService**: Main service for order operations
2. **OrderKafkaProducerService**: Publishes order and payment events
3. **OrderKafkaConsumerService**: Consumes events from other services
4. **OrderEvent**: Event model for order-related activities
5. **PaymentEvent**: Event model for payment-related activities

### Kafka Topics

#### Producer Topics (Order Service → Other Services)
- `order-events`: General order events
- `order-status-updates`: Order status change notifications
- `payment-events`: Payment-related events
- `payment-verification`: Payment verification requests

#### Consumer Topics (Other Services → Order Service)
- `payment-verification-response`: Payment verification responses
- `payment-status-updates`: Payment status change notifications
- `user-status-updates`: User account status changes

## API Endpoints

### Order Management
- `POST /api/orders` - Create new order from cart
- `PUT /api/orders/{orderId}/status` - Update order status
- `PUT /api/orders/{orderId}/payment-status` - Update payment status
- `GET /api/orders/{orderId}/tracking` - Get order tracking information
- `GET /api/orders/user/{userId}` - Get user's order history

### Request/Response Examples

#### Create Order
```json
POST /api/orders
{
  "userId": "user-123",
  "orderData": {
    "paymentMethod": "CREDIT_CARD",
    "deliveryAddress": "123 Main St",
    "deliveryCity": "New York",
    "deliveryState": "NY",
    "deliveryZipCode": "10001",
    "deliveryCountry": "USA",
    "deliveryPhone": "+1-555-0101"
  }
}
```

#### Order Status Update
```json
PUT /api/orders/{orderId}/status
{
  "status": "CONFIRMED",
  "userId": "user-123"
}
```

## Event Flow

### Order Creation Flow
1. User submits order from cart
2. OrderManagementService creates order
3. Publishes `ORDER_CREATED` event
4. Sends payment verification request
5. Payment Service processes payment
6. Payment Service sends verification response
7. Order status updated based on payment result

### Order Status Update Flow
1. Order status changes (e.g., shipped, delivered)
2. OrderManagementService updates status
3. Publishes appropriate status event
4. Other services consume event for their needs

### Payment Status Flow
1. Payment Service processes payment
2. Payment Service publishes status update
3. OrderKafkaConsumerService consumes update
4. Order status automatically updated
5. Order status event published if needed

## Configuration

### Kafka Configuration
```properties
# Kafka Topics
kafka.topic.order-events=order-events
kafka.topic.payment-events=payment-events
kafka.topic.order-status-updates=order-status-updates
kafka.topic.payment-verification=payment-verification
kafka.topic.payment-verification-response=payment-verification-response
kafka.topic.payment-status-updates=payment-status-updates
kafka.topic.user-status-updates=user-status-updates
```

### Database Configuration
- Uses JPA/Hibernate for data persistence
- Supports MySQL and H2 databases
- Automatic schema generation and updates

## Dependencies

### Core Dependencies
- Spring Boot 3.5.5
- Spring Data JPA
- Spring Kafka
- MySQL/H2 Database
- Jackson for JSON serialization

### Service Dependencies
- OrderRepository: Data access for orders
- CartRepository: Shopping cart operations
- UserRepository: User management
- ProductRepository: Product information

## Error Handling

### Exception Types
- **OrderNotFoundException**: Order not found in database
- **UnauthorizedException**: User not authorized for operation
- **InvalidOrderDataException**: Invalid order data provided
- **PaymentVerificationException**: Payment verification failed

### Error Responses
```json
{
  "success": false,
  "error": "Order not found",
  "message": "Order with ID order-123 not found",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Monitoring and Logging

### Logging Levels
- **DEBUG**: Detailed operation information
- **INFO**: General operation status
- **WARN**: Warning conditions
- **ERROR**: Error conditions with stack traces

### Metrics
- Order creation rate
- Order status transition times
- Kafka event publishing success rate
- Payment verification response times

## Testing

### Unit Tests
- Service layer testing with mocked dependencies
- Event publishing verification
- Business logic validation

### Integration Tests
- Kafka event flow testing
- Database transaction testing
- End-to-end order flow testing

### Test Data
- Sample orders with various statuses
- Mock payment responses
- User status change scenarios

## Deployment

### Prerequisites
- Kafka cluster running
- MySQL database accessible
- Java 17+ runtime

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
JWT_SECRET=your-secret-key
```

### Health Checks
- Database connectivity
- Kafka connectivity
- Service readiness

## Future Enhancements

### Planned Features
- **Order Analytics**: Order performance metrics
- **Inventory Integration**: Real-time stock updates
- **Shipping Integration**: Carrier API integration
- **Notification Service**: Email/SMS order updates
- **Order Recommendations**: AI-powered suggestions

### Scalability Improvements
- **Event Sourcing**: Complete order event history
- **CQRS**: Separate read/write models
- **Microservices**: Split into smaller services
- **Caching**: Redis for frequently accessed data

## Support and Maintenance

### Troubleshooting
- Check Kafka connectivity
- Verify database connections
- Review application logs
- Monitor event publishing

### Performance Tuning
- Kafka producer/consumer tuning
- Database query optimization
- Connection pooling configuration
- JVM memory settings

## Contributing
1. Follow Spring Boot best practices
2. Maintain comprehensive test coverage
3. Update documentation for changes
4. Follow event-driven architecture patterns
5. Ensure proper error handling and logging
