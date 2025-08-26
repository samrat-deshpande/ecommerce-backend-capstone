# Database Configuration Guide

This project supports multiple database configurations through Spring profiles.

## Default Configuration (H2 Database)

The application runs with **H2 in-memory database by default** for development and testing.

**Features:**
- In-memory database (data is lost when application restarts)
- H2 Console available at: http://localhost:8080/h2-console
- Automatic schema creation and data loading
- No external database setup required

**To run with default H2 configuration:**
```bash
./gradlew bootRun
# or
java -jar build/libs/ecommerce-*.jar
```

## MySQL Profile

To use MySQL database instead of H2, activate the MySQL profile.

**Prerequisites:**
1. MySQL server running on localhost:3306
2. Database `ecommerce` created
3. User credentials configured

**To run with MySQL:**
```bash
./gradlew bootRun --args='--spring.profiles.active=mysql'
# or
java -jar build/libs/ecommerce-*.jar --spring.profiles.active=mysql
```

**MySQL Configuration:**
- Host: localhost:3306
- Database: ecommerce
- Username: root
- Password: password

## Production Profile

For production deployment, use the production profile with environment variables.

**To run with production profile:**
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
# or
java -jar build/libs/ecommerce-*.jar --spring.profiles.active=prod
```

**Environment Variables (optional):**
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka server addresses
- `JWT_SECRET`: JWT signing secret
- `MAIL_HOST`: SMTP server host
- `MAIL_USERNAME`: Email username
- `MAIL_PASSWORD`: Email password
- `STRIPE_SECRET_KEY`: Stripe secret key
- `STRIPE_PUBLISHABLE_KEY`: Stripe publishable key
- `STRIPE_WEBHOOK_SECRET`: Stripe webhook secret

## Test Profile

For running tests, the application automatically uses the test profile with H2 database.

**Test Configuration:**
- H2 in-memory database
- Kafka disabled (mocked)
- Sample data loaded automatically
- Schema created and dropped for each test

## Switching Between Profiles

### Development (H2 - Default)
```bash
./gradlew bootRun
```

### MySQL Development
```bash
./gradlew bootRun --args='--spring.profiles.active=mysql'
```

### Production
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Testing
```bash
./gradlew test
```

## H2 Console Access

When using H2 database, you can access the H2 console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:ecommercedb
- Username: sa
- Password: (leave empty)

## Data Persistence

- **H2 (Default)**: Data is lost when application restarts
- **MySQL**: Data persists between application restarts
- **Test**: Data is created fresh for each test run

## Troubleshooting

### H2 Database Issues
- Ensure H2 dependency is in build.gradle
- Check H2 console is accessible
- Verify schema creation logs

### MySQL Connection Issues
- Verify MySQL server is running
- Check database exists: `CREATE DATABASE ecommerce;`
- Verify user credentials
- Check MySQL connector dependency

### Profile Issues
- Verify profile name is correct
- Check profile-specific properties file exists
- Ensure no conflicting properties
