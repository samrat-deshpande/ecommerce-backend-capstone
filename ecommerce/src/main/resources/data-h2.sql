-- H2 Database Test Data Initialization
-- This script will be executed when the application starts with H2 profile
-- Updated to match the correct User entity structure

-- Clear existing data (if any)
DELETE FROM payments;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM cart_items;
DELETE FROM carts;
DELETE FROM products;
DELETE FROM categories;
DELETE FROM password_reset_tokens;
DELETE FROM users;

-- Insert sample categories
INSERT INTO categories (id, name, description, parent_id, image_url, icon_class, sort_order, product_count, active, featured, meta_title, meta_description, meta_keywords, created_at, updated_at) VALUES
('cat-001', 'Electronics', 'Electronic devices and accessories', NULL, '/images/categories/electronics.jpg', 'fas fa-mobile-alt', 1, 0, true, true, 'Electronics Store', 'Shop the latest electronics and gadgets', 'electronics, gadgets, devices, tech', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cat-002', 'Smartphones', 'Mobile phones and smartphones', 'cat-001', '/images/categories/smartphones.jpg', 'fas fa-mobile-alt', 1, 0, true, true, 'Smartphones', 'Latest smartphones and mobile phones', 'smartphones, mobile, phones, android, iphone', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cat-003', 'Laptops', 'Portable computers and laptops', 'cat-001', '/images/categories/laptops.jpg', 'fas fa-laptop', 2, 0, true, true, 'Laptops', 'High-performance laptops and computers', 'laptops, computers, portable, gaming, business', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cat-004', 'Clothing', 'Fashion and apparel for all ages', NULL, '/images/categories/clothing.jpg', 'fas fa-tshirt', 2, 0, true, true, 'Fashion Store', 'Trendy clothing and accessories', 'clothing, fashion, apparel, style', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cat-005', 'Books', 'Books, magazines, and educational materials', NULL, '/images/categories/books.jpg', 'fas fa-book', 4, 0, true, false, 'Bookstore', 'Wide selection of books and educational materials', 'books, reading, education, fiction, non-fiction', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert sample products
INSERT INTO products (id, name, description, price, original_price, discount_percentage, category, sub_category, brand, model, sku, stock_quantity, min_stock_level, weight, dimensions, image_url, average_rating, review_count, featured, active, created_at, updated_at) VALUES
('prod-001', 'iPhone 15 Pro', 'Latest iPhone with advanced camera system and A17 Pro chip', 999.99, 1099.99, 9.09, 'Electronics', 'Smartphones', 'Apple', 'iPhone 15 Pro', 'IPH15PRO-128', 50, 10, 0.187, '146.7 x 71.5 x 8.25 mm', '/images/products/iphone15pro.jpg', 4.8, 125, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('prod-002', 'Samsung Galaxy S24', 'Flagship Android smartphone with AI features', 899.99, 899.99, NULL, 'Electronics', 'Smartphones', 'Samsung', 'Galaxy S24', 'SAMS24-256', 45, 10, 0.168, '147.0 x 70.6 x 7.6 mm', '/images/products/galaxys24.jpg', 4.6, 89, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('prod-003', 'MacBook Pro 14"', 'Professional laptop with M3 Pro chip', 1999.99, 2199.99, 9.09, 'Electronics', 'Laptops', 'Apple', 'MacBook Pro 14"', 'MBP14-M3PRO-512', 25, 5, 1.6, '312.6 x 221.2 x 15.5 mm', '/images/products/macbookpro14.jpg', 4.9, 67, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('prod-004', 'Men''s Casual T-Shirt', 'Comfortable cotton t-shirt for everyday wear', 24.99, 29.99, 16.67, 'Clothing', 'Men''s Clothing', 'Fashion Brand', 'Casual T-Shirt', 'MENS-TSHIRT-CASUAL', 100, 20, 0.2, 'M, L, XL', '/images/products/mens-tshirt.jpg', 4.3, 156, false, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('prod-005', 'The Great Gatsby', 'Classic novel by F. Scott Fitzgerald', 12.99, 12.99, NULL, 'Books', NULL, 'Scribner', 'Paperback', 'BOOK-GATSBY-PAPERBACK', 200, 50, 0.3, '5.2 x 0.8 x 8 inches', '/images/products/gatsby-book.jpg', 4.7, 234, false, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Update category product counts
UPDATE categories SET product_count = 3 WHERE id = 'cat-001';
UPDATE categories SET product_count = 2 WHERE id = 'cat-002';
UPDATE categories SET product_count = 1 WHERE id = 'cat-003';
UPDATE categories SET product_count = 1 WHERE id = 'cat-004';
UPDATE categories SET product_count = 1 WHERE id = 'cat-005';

-- Insert sample users with CORRECT password hash for 'password123'
-- This hash was generated using BCrypt with strength 10 for the password 'password123'
INSERT INTO users (id, email, password, first_name, last_name, phone_number, role, active, email_verified, phone_verified, created_at, updated_at) VALUES
('user-001', 'john.doe@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Doe', '+1-555-0101', 'USER', true, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('user-002', 'jane.smith@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', '+1-555-0102', 'USER', true, true, false, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('user-003', 'mike.johnson@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Mike', 'Johnson', '+1-555-0103', 'USER', true, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('user-004', 'admin@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', '+1-555-0000', 'ADMIN', true, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('user-005', 'moderator@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Moderator', 'User', '+1-555-0001', 'MODERATOR', true, true, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert sample carts
INSERT INTO carts (id, user_id, total_amount, item_count, status, created_at, updated_at) VALUES
('cart-001', 'user-001', 0.00, 0, 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cart-002', 'user-002', 0.00, 0, 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cart-003', 'user-003', 0.00, 0, 'ACTIVE', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert sample cart items
INSERT INTO cart_items (id, cart_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('cartitem-001', 'cart-001', 'prod-001', 'iPhone 15 Pro', '/images/products/iphone15pro.jpg', 1, 999.99, 999.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cartitem-002', 'cart-001', 'prod-004', 'Men''s Casual T-Shirt', '/images/products/mens-tshirt.jpg', 2, 24.99, 49.98, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('cartitem-003', 'cart-002', 'prod-003', 'MacBook Pro 14"', '/images/products/macbookpro14.jpg', 1, 1999.99, 1999.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Update cart totals
UPDATE carts SET total_amount = 1049.97, item_count = 2 WHERE id = 'cart-001';
UPDATE carts SET total_amount = 1999.99, item_count = 1 WHERE id = 'cart-002';

-- Insert sample orders
INSERT INTO orders (id, order_number, user_id, subtotal, tax_amount, shipping_amount, total_amount, status, payment_status, payment_method, created_at, updated_at) VALUES
('order-001', 'ORD-2024-001', 'user-001', 1049.97, 104.99, 15.00, 1169.96, 'CONFIRMED', 'PAID', 'CREDIT_CARD', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('order-002', 'ORD-2024-002', 'user-002', 1999.99, 199.99, 25.00, 2224.98, 'SHIPPED', 'PAID', 'CREDIT_CARD', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert sample order items
INSERT INTO order_items (id, order_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('orderitem-001', 'order-001', 'prod-001', 'iPhone 15 Pro', '/images/products/iphone15pro.jpg', 1, 999.99, 999.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('orderitem-002', 'order-001', 'prod-004', 'Men''s Casual T-Shirt', '/images/products/mens-tshirt.jpg', 2, 24.99, 49.98, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('orderitem-003', 'order-002', 'prod-003', 'MacBook Pro 14"', '/images/products/macbookpro14.jpg', 1, 1999.99, 1999.99, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert sample payments
INSERT INTO payments (id, order_id, user_id, amount, payment_method, status, transaction_id, gateway_response, card_last_four, card_brand, card_expiry_month, card_expiry_year, billing_address, billing_city, billing_state, billing_zip_code, billing_country, processed_at, created_at, updated_at) VALUES
('payment-001', 'order-001', 'user-001', 1169.96, 'CREDIT_CARD', 'PROCESSED', 'TXN123456789', 'Approved', '1234', 'VISA', 12, 2026, '123 Main Street', 'New York', 'NY', '10001', 'USA', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('payment-002', 'order-002', 'user-002', 2224.98, 'CREDIT_CARD', 'PROCESSED', 'TXN987654321', 'Approved', '5678', 'MASTERCARD', 8, 2025, '456 Oak Avenue', 'Los Angeles', 'CA', '90210', 'USA', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
