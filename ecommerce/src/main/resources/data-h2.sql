-- H2 Database Test Data Initialization
-- This script will be executed when the application starts with H2 profile

-- Insert sample categories
INSERT INTO categories (id, name, description, image_url, created_at, updated_at) VALUES
('cat-001', 'Electronics', 'Electronic devices and accessories', 'https://example.com/electronics.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-002', 'Clothing', 'Fashion and apparel', 'https://example.com/clothing.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-003', 'Books', 'Books and publications', 'https://example.com/books.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample products
INSERT INTO products (id, name, description, price, stock_quantity, category_id, image_url, created_at, updated_at) VALUES
('prod-001', 'Laptop', 'High-performance laptop for work and gaming', 999.99, 50, 'cat-001', 'https://example.com/laptop.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('prod-002', 'Smartphone', 'Latest smartphone with advanced features', 699.99, 100, 'cat-001', 'https://example.com/smartphone.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('prod-003', 'T-Shirt', 'Comfortable cotton t-shirt', 19.99, 200, 'cat-002', 'https://example.com/tshirt.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('prod-004', 'Programming Book', 'Learn Java programming', 49.99, 75, 'cat-003', 'https://example.com/book.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample users
INSERT INTO users (id, username, email, password, first_name, last_name, phone, role, is_active, created_at, updated_at) VALUES
('user-001', 'john_doe', 'john@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'John', 'Doe', '+1234567890', 'USER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user-002', 'jane_smith', 'jane@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Jane', 'Smith', '+1234567891', 'USER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin-001', 'admin', 'admin@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin', 'User', '+1234567892', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample carts
INSERT INTO carts (id, user_id, status, total_amount, created_at, updated_at) VALUES
('cart-001', 'user-001', 'ACTIVE', 1019.98, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cart-002', 'user-002', 'ACTIVE', 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample cart items
INSERT INTO cart_items (id, cart_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('item-001', 'cart-001', 'prod-001', 'Laptop', 'https://example.com/laptop.jpg', 1, 999.99, 999.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('item-002', 'cart-001', 'prod-003', 'T-Shirt', 'https://example.com/tshirt.jpg', 1, 19.99, 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('item-003', 'cart-002', 'prod-003', 'T-Shirt', 'https://example.com/tshirt.jpg', 1, 19.99, 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample orders
INSERT INTO orders (id, user_id, order_number, status, payment_status, payment_method, subtotal, tax_amount, shipping_amount, total_amount, delivery_address, delivery_city, delivery_state, delivery_zip_code, delivery_country, created_at, updated_at) VALUES
('order-001', 'user-001', 'ORD-20241201120000-ABC12345', 'CONFIRMED', 'PAID', 'CREDIT_CARD', 1019.98, 101.99, 15.00, 1136.97, '123 Main St', 'New York', 'NY', '10001', 'USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('order-002', 'user-002', 'ORD-20241201120001-DEF67890', 'PENDING', 'PENDING', 'DIGITAL_WALLET', 49.99, 5.00, 15.00, 69.99, '456 Oak Ave', 'Los Angeles', 'CA', '90210', 'USA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample order items
INSERT INTO order_items (id, order_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('oi-001', 'order-001', 'prod-001', 'Laptop', 'https://example.com/laptop.jpg', 1, 999.99, 999.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('oi-002', 'order-001', 'prod-003', 'T-Shirt', 'https://example.com/tshirt.jpg', 1, 19.99, 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('oi-003', 'order-002', 'prod-004', 'Programming Book', 'https://example.com/book.jpg', 1, 49.99, 49.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample payments
INSERT INTO payments (id, order_id, user_id, amount, payment_method, status, transaction_id, gateway_response, created_at, updated_at, processed_at) VALUES
('payment-001', 'order-001', 'user-001', 1136.97, 'CREDIT_CARD', 'SUCCESSFUL', 'txn_123456789', 'Payment successful', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('payment-002', 'order-002', 'user-002', 69.99, 'DIGITAL_WALLET', 'PROCESSING', NULL, 'Payment processing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);
