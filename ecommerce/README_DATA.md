# Sample Data for Ecommerce Application

This document explains how to use the sample data script to populate your ecommerce application database with realistic test data.

## Overview

The `src/main/resources/data.sql` file contains comprehensive sample data for all tables in your ecommerce application:

- **Users**: 6 users (including admin and moderator roles)
- **Categories**: 8 categories with hierarchical structure
- **Products**: 15 products across different categories
- **Carts**: 3 active shopping carts
- **Cart Items**: 6 cart items
- **Orders**: 5 orders in various states
- **Order Items**: 6 order items
- **Payments**: 5 payment records

## Database Setup

### Option 1: Spring Boot Auto-execution

If you're using Spring Boot with JPA/Hibernate, you can configure the application to automatically execute this script:

```properties
# In application.properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

### Option 2: Manual Database Execution

1. **Start your application** and let it create the database schema
2. **Execute the data.sql script** in your database:

```bash
# For MySQL/MariaDB
mysql -u username -p database_name < src/main/resources/data.sql

# For PostgreSQL
psql -U username -d database_name -f src/main/resources/data.sql

# For H2 (if using H2 console)
# Copy and paste the SQL content into the H2 console
```

### Option 3: Database Tool Execution

Use your preferred database management tool (DBeaver, pgAdmin, MySQL Workbench, etc.) to execute the script.

## Sample Data Details

### Users
- **Regular Users**: john.doe@example.com, jane.smith@example.com, mike.johnson@example.com, sarah.wilson@example.com
- **Admin**: admin@ecommerce.com
- **Moderator**: moderator@ecommerce.com
- **Password**: All users have the same encrypted password (for testing purposes)

### Categories
- **Electronics** (with subcategories: Smartphones, Laptops)
- **Clothing** (with subcategories: Men's Clothing, Women's Clothing)
- **Home & Garden**
- **Books**

### Products
- **Electronics**: iPhone 15 Pro, Samsung Galaxy S24, MacBook Pro, Dell XPS 13, Bluetooth Headphones, Smart Watch
- **Clothing**: Men's T-Shirt, Women's Summer Dress, Running Shoes
- **Home & Garden**: Garden Tool Set, Yoga Mat, Coffee Maker
- **Books**: The Great Gatsby

### Sample Orders
- **Order 1**: iPhone 15 Pro + T-Shirt (Confirmed, Paid)
- **Order 2**: MacBook Pro 14" (Shipped, Paid)
- **Order 3**: Women's Summer Dress (Delivered, Paid)
- **Order 4**: Garden Tool Set (Processing, Authorized)
- **Order 5**: Bluetooth Headphones (Pending, Pending)

## Data Relationships

The sample data maintains proper referential integrity:

- Cart items reference valid products and carts
- Order items reference valid products and orders
- Payments reference valid orders and users
- Categories have proper parent-child relationships
- Product counts are updated based on actual products

## Customization

You can modify the sample data by:

1. **Adding more products** with realistic details
2. **Creating additional users** with different roles
3. **Expanding categories** for your specific business domain
4. **Modifying prices** to match your market
5. **Adding more orders** to test different scenarios

## Testing Scenarios

This sample data supports testing of:

- **User Management**: Login, registration, role-based access
- **Product Catalog**: Browsing, searching, filtering
- **Shopping Cart**: Adding/removing items, quantity updates
- **Order Management**: Order creation, status updates, tracking
- **Payment Processing**: Different payment methods and statuses
- **Admin Functions**: User management, order oversight

## Notes

- **Timestamps**: All records use `CURRENT_TIMESTAMP` for creation/update times
- **IDs**: Use UUID format for consistency with your entity design
- **Passwords**: Encrypted with BCrypt (same hash for all users for testing)
- **Images**: Placeholder paths that you can replace with actual images
- **Addresses**: Fictional addresses for testing purposes

## Troubleshooting

### Common Issues

1. **Foreign Key Constraints**: Ensure the script runs after table creation
2. **Data Types**: Verify your database supports the data types used
3. **Timestamp Functions**: Some databases may require different timestamp syntax
4. **Character Encoding**: Ensure proper UTF-8 support for special characters

### Database-Specific Adjustments

- **MySQL**: The script should work as-is
- **PostgreSQL**: May need to adjust `INTERVAL` syntax
- **H2**: Should work with minimal adjustments
- **SQL Server**: May need timestamp and interval syntax adjustments

## Security Note

⚠️ **Important**: This sample data is for development/testing only. Never use these credentials in production. The encrypted passwords are the same for all users and should be changed for production use.

## Support

If you encounter issues with the sample data script, check:

1. Database schema matches your entity definitions
2. Database supports all data types used
3. Proper database connection and permissions
4. Script execution order (schema before data)
