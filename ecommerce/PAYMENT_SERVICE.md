# Payment Service

## Overview
The Payment Service is a comprehensive service that manages payment gateways, transaction logs, and produces Kafka messages to notify the Order Management Service once payments are confirmed. It provides a unified interface for processing payments through multiple payment gateways while maintaining detailed transaction logs.

## Features

### Core Functionality
- **Payment Processing**: Process payments through multiple payment gateways
- **Gateway Management**: Support for multiple payment gateways (Stripe, PayPal, etc.)
- **Transaction Logging**: Comprehensive logging of all payment transactions
- **Refund Processing**: Handle full and partial refunds
- **Payment Verification**: Verify payment transactions through gateways
- **Status Management**: Track payment status throughout the lifecycle

### Kafka Integration
- **Event Publishing**: Automatically publish events for all payment state changes
- **Order Notification**: Notify Order Management Service when payments are confirmed
- **Real-time Updates**: Enable real-time synchronization between services
- **Payment Verification**: Handle payment verification requests from Order Service

## Architecture

### Service Components
1. **PaymentService**: Main service interface for payment operations
2. **PaymentGatewayService**: Interface for different payment gateways
3. **StripePaymentGatewayService**: Stripe payment gateway implementation
4. **PaymentKafkaProducerService**: Publishes payment events to Kafka
5. **PaymentKafkaConsumerService**: Consumes payment verification requests
6. **PaymentServiceImpl**: Main service implementation with gateway integration

### Payment Gateway Support
- **Stripe**: Credit cards, digital wallets, bank transfers
- **Extensible**: Easy to add new payment gateways
- **Fallback Support**: Multiple gateway options for redundancy
- **Gateway Health Monitoring**: Real-time gateway status checks

### Kafka Topics

#### Producer Topics (Payment Service → Other Services)
- `payment-events`: General payment events
- `payment-status-updates`: Payment status change notifications
- `payment-verification-response`: Payment verification responses
- `payment-refunds`: Payment refund notifications

#### Consumer Topics (Other Services → Payment Service)
- `payment-verification`: Payment verification requests from Order Service

## API Endpoints

### Payment Processing
- `POST /api/payments/process` - Process payment for an order
- `POST /api/payments/verify` - Verify payment transaction
- `POST /api/payments/refund` - Process payment refund
- `GET /api/payments/{paymentId}` - Get payment details
- `GET /api/payments/order/{orderId}` - Get payment by order ID
- `GET /api/payments/user/{userId}/history` - Get user payment history
- `PUT /api/payments/{paymentId}/status` - Update payment status
- `GET /api/payments/gateway/status` - Get gateway status
- `GET /api/payments/gateway/test` - Test gateway connectivity

### Request/Response Examples

#### Process Payment
```json
POST /api/payments/process
{
  "orderId": "order-123",
  "userId": "user-456",
  "amount": 99.99,
  "paymentMethod": "CREDIT_CARD",
  "paymentData": {
    "cardNumber": "4242424242424242",
    "expiryMonth": "12",
    "expiryYear": "2026",
    "cvv": "123",
    "cardholderName": "John Doe"
  }
}
```

#### Payment Response
```json
{
  "success": true,
  "paymentId": "payment-789",
  "transactionId": "stripe_1234567890_123",
  "status": "SUCCESSFUL",
  "amount": 99.99,
  "gatewayResponse": "Payment processed successfully"
}
```

#### Process Refund
```json
POST /api/payments/refund
{
  "paymentId": "payment-789",
  "orderId": "order-123",
  "amount": 99.99,
  "reason": "Customer requested refund"
}
```

## Event Flow

### Payment Processing Flow
1. Order Management Service sends payment verification request
2. Payment Service receives request via Kafka consumer
3. Payment Service processes payment through gateway
4. Payment Service publishes payment status events
5. Order Management Service receives payment confirmation
6. Order status updated based on payment result

### Payment Status Flow
1. Payment initiated (ORDER_CREATED event received)
2. Payment processing (gateway communication)
3. Payment successful/failed (gateway response)
4. Payment status event published
5. Order Management Service notified
6. Order status updated accordingly

### Refund Flow
1. Refund request received
2. Refund processed through gateway
3. Payment status updated to REFUNDED
4. Refund event published via Kafka
5. Order Management Service notified
6. Order status updated if needed

## Configuration

### Kafka Configuration
```properties
# Payment Service Kafka Topics
kafka.topic.payment-events=payment-events
kafka.topic.payment-status-updates=payment-status-updates
kafka.topic.payment-verification-response=payment-verification-response
kafka.topic.payment-refunds=payment-refunds
kafka.topic.payment-verification=payment-verification
```

### Stripe Configuration
```properties
# Stripe Payment Gateway
stripe.secret-key=sk_test_your_stripe_secret_key_here
stripe.publishable-key=pk_test_your_stripe_publishable_key_here
stripe.webhook-secret=whsec_your_stripe_webhook_secret_here
```

### Database Configuration
- Uses JPA/Hibernate for data persistence
- Supports MySQL and H2 databases
- Automatic schema generation and updates
- Transaction management for payment operations

## Dependencies

### Core Dependencies
- Spring Boot 3.5.5
- Spring Data JPA
- Spring Kafka
- MySQL/H2 Database
- Jackson for JSON serialization

### Service Dependencies
- PaymentRepository: Data access for payments
- UserRepository: User management
- PaymentGatewayService: Gateway abstraction
- KafkaProducerService: Event publishing

## Payment Gateway Integration

### Stripe Gateway
- **Supported Methods**: Credit cards, debit cards, digital wallets, bank transfers
- **Card Types**: Visa, Mastercard, American Express, Discover
- **Currencies**: USD, EUR, GBP, CAD, AUD
- **Features**: 3D Secure, SCA compliance, webhook support

### Adding New Gateways
1. Implement `PaymentGatewayService` interface
2. Add gateway configuration properties
3. Register as Spring service with qualifier
4. Update service configuration

## Error Handling

### Exception Types
- **PaymentProcessingException**: Payment processing failed
- **GatewayException**: Gateway communication error
- **InvalidPaymentDataException**: Invalid payment data
- **RefundException**: Refund processing failed
- **UnauthorizedException**: User not authorized for operation

### Error Responses
```json
{
  "success": false,
  "error": "Payment processing failed",
  "message": "Gateway communication error",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Monitoring and Logging

### Logging Levels
- **DEBUG**: Detailed payment processing information
- **INFO**: General payment status updates
- **WARN**: Payment failures and retries
- **ERROR**: Processing errors and exceptions

### Metrics
- Payment success/failure rates
- Gateway response times
- Transaction processing times
- Refund processing rates
- Kafka event publishing success rate

## Security

### Data Protection
- **PCI Compliance**: Secure handling of payment data
- **Encryption**: Sensitive data encryption at rest and in transit
- **Tokenization**: Payment data tokenization for security
- **Access Control**: User authorization for payment operations

### Audit Trail
- Complete payment transaction logs
- User action tracking
- Gateway communication logs
- Refund processing history

## Testing

### Unit Tests
- Service layer testing with mocked dependencies
- Gateway integration testing
- Event publishing verification
- Business logic validation

### Integration Tests
- Kafka event flow testing
- Database transaction testing
- Gateway connectivity testing
- End-to-end payment flow testing

### Test Data
- Sample payment transactions
- Mock gateway responses
- Various payment methods
- Error scenarios

## Deployment

### Prerequisites
- Kafka cluster running
- MySQL database accessible
- Java 17+ runtime
- Payment gateway accounts configured

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
STRIPE_PUBLISHABLE_KEY=pk_test_your_stripe_publishable_key
STRIPE_WEBHOOK_SECRET=whsec_your_stripe_webhook_secret
```

### Health Checks
- Database connectivity
- Kafka connectivity
- Gateway connectivity
- Service readiness

## Future Enhancements

### Planned Features
- **Multi-Gateway Support**: PayPal, Square, Adyen integration
- **Recurring Payments**: Subscription and installment support
- **Fraud Detection**: AI-powered fraud prevention
- **Payment Analytics**: Payment performance metrics
- **Webhook Management**: Dynamic webhook configuration

### Scalability Improvements
- **Async Processing**: Non-blocking payment processing
- **Caching**: Redis for frequently accessed data
- **Load Balancing**: Multiple gateway instances
- **Circuit Breaker**: Gateway failure handling
- **Rate Limiting**: API request throttling

## Support and Maintenance

### Troubleshooting
- Check gateway connectivity
- Verify Kafka topics
- Review payment logs
- Monitor gateway responses
- Check database connections

### Performance Tuning
- Gateway connection pooling
- Database query optimization
- Kafka producer/consumer tuning
- JVM memory settings
- Async processing configuration

## Contributing
1. Follow Spring Boot best practices
2. Maintain comprehensive test coverage
3. Update documentation for changes
4. Follow payment security guidelines
5. Ensure proper error handling and logging
6. Test with multiple payment gateways
