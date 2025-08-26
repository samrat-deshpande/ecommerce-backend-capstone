-- Sample Data for Ecommerce Application (MySQL Version)
-- This script populates all tables with realistic sample data for MySQL
-- Compatible with MySQL 8.0+ and other MySQL-compatible databases

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

-- Insert Users
INSERT INTO users (id, email, password, first_name, last_name, phone_number, role, active, email_verified, phone_verified, created_at, updated_at) VALUES
('user-001', 'john.doe@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Doe', '+1-555-0101', 'USER', true, true, true, NOW(), NOW()),
('user-002', 'jane.smith@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', '+1-555-0102', 'USER', true, true, false, NOW(), NOW()),
('user-003', 'mike.johnson@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Mike', 'Johnson', '+1-555-0103', 'USER', true, true, true, NOW(), NOW()),
('user-004', 'sarah.wilson@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Sarah', 'Wilson', '+1-555-0104', 'USER', true, false, false, NOW(), NOW()),
('user-005', 'admin@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', '+1-555-0000', 'ADMIN', true, true, true, NOW(), NOW()),
('user-006', 'moderator@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Moderator', 'User', '+1-555-0001', 'MODERATOR', true, true, true, NOW(), NOW());

-- Insert Categories (Hierarchical structure)
INSERT INTO categories (id, name, description, parent_id, image_url, icon_class, sort_order, product_count, active, featured, meta_title, meta_description, meta_keywords, created_at, updated_at) VALUES
('cat-001', 'Electronics', 'Latest electronic gadgets and devices', NULL, '/images/categories/electronics.jpg', 'fas fa-mobile-alt', 1, 0, true, true, 'Electronics Store', 'Shop the latest electronics and gadgets', 'electronics, gadgets, devices, tech', NOW(), NOW()),
('cat-002', 'Smartphones', 'Mobile phones and smartphones', 'cat-001', '/images/categories/smartphones.jpg', 'fas fa-mobile-alt', 1, 0, true, true, 'Smartphones', 'Latest smartphones and mobile phones', 'smartphones, mobile, phones, android, iphone', NOW(), NOW()),
('cat-003', 'Laptops', 'Portable computers and laptops', 'cat-001', '/images/categories/laptops.jpg', 'fas fa-laptop', 2, 0, true, true, 'Laptops', 'High-performance laptops and computers', 'laptops, computers, portable, gaming, business', NOW(), NOW()),
('cat-004', 'Clothing', 'Fashion and apparel for all ages', NULL, '/images/categories/clothing.jpg', 'fas fa-tshirt', 2, 0, true, true, 'Fashion Store', 'Trendy clothing and accessories', 'clothing, fashion, apparel, style', NOW(), NOW()),
('cat-005', 'Men''s Clothing', 'Men''s fashion and apparel', 'cat-004', '/images/categories/mens-clothing.jpg', 'fas fa-male', 1, 0, true, false, 'Men''s Clothing', 'Stylish men''s clothing and accessories', 'mens clothing, fashion, style, casual, formal', NOW(), NOW()),
('cat-006', 'Women''s Clothing', 'Women''s fashion and apparel', 'cat-004', '/images/categories/womens-clothing.jpg', 'fas fa-female', 2, 0, true, false, 'Women''s Clothing', 'Elegant women''s clothing and accessories', 'womens clothing, fashion, style, dresses, tops', NOW(), NOW()),
('cat-007', 'Home & Garden', 'Home improvement and garden supplies', NULL, '/images/categories/home-garden.jpg', 'fas fa-home', 3, 0, true, false, 'Home & Garden', 'Everything for your home and garden', 'home, garden, furniture, decor, tools', NOW(), NOW()),
('cat-008', 'Books', 'Books, magazines, and educational materials', NULL, '/images/categories/books.jpg', 'fas fa-book', 4, 0, true, false, 'Bookstore', 'Wide selection of books and educational materials', 'books, reading, education, fiction, non-fiction', NOW(), NOW());

-- Insert Products
INSERT INTO products (id, name, description, price, original_price, discount_percentage, category, sub_category, brand, model, sku, stock_quantity, min_stock_level, weight, dimensions, image_url, average_rating, review_count, featured, active, created_at, updated_at) VALUES
('prod-001', 'iPhone 15 Pro', 'Latest iPhone with advanced camera system and A17 Pro chip', 999.99, 1099.99, 9.09, 'Electronics', 'Smartphones', 'Apple', 'iPhone 15 Pro', 'IPH15PRO-128', 50, 10, 0.187, '146.7 x 71.5 x 8.25 mm', '/images/products/iphone15pro.jpg', 4.8, 125, true, true, NOW(), NOW()),
('prod-002', 'Samsung Galaxy S24', 'Flagship Android smartphone with AI features', 899.99, 899.99, NULL, 'Electronics', 'Smartphones', 'Samsung', 'Galaxy S24', 'SAMS24-256', 45, 10, 0.168, '147.0 x 70.6 x 7.6 mm', '/images/products/galaxys24.jpg', 4.6, 89, true, true, NOW(), NOW()),
('prod-003', 'MacBook Pro 14"', 'Professional laptop with M3 Pro chip', 1999.99, 2199.99, 9.09, 'Electronics', 'Laptops', 'Apple', 'MacBook Pro 14"', 'MBP14-M3PRO-512', 25, 5, 1.6, '312.6 x 221.2 x 15.5 mm', '/images/products/macbookpro14.jpg', 4.9, 67, true, true, NOW(), NOW()),
('prod-004', 'Dell XPS 13', 'Ultrabook with Intel Core i7 processor', 1299.99, 1299.99, NULL, 'Electronics', 'Laptops', 'Dell', 'XPS 13', 'DELLXPS13-I7-512', 30, 5, 1.2, '302 x 199 x 14.8 mm', '/images/products/dellxps13.jpg', 4.7, 43, false, true, NOW(), NOW()),
('prod-005', 'Men''s Casual T-Shirt', 'Comfortable cotton t-shirt for everyday wear', 24.99, 29.99, 16.67, 'Clothing', 'Men''s Clothing', 'Fashion Brand', 'Casual T-Shirt', 'MENS-TSHIRT-CASUAL', 100, 20, 0.2, 'M, L, XL', '/images/products/mens-tshirt.jpg', 4.3, 156, false, true, NOW(), NOW()),
('prod-006', 'Women''s Summer Dress', 'Elegant summer dress with floral pattern', 59.99, 79.99, 25.00, 'Clothing', 'Women''s Clothing', 'Elegance', 'Summer Dress', 'WOMENS-DRESS-SUMMER', 75, 15, 0.3, 'XS, S, M, L', '/images/products/womens-dress.jpg', 4.5, 89, true, true, NOW(), NOW()),
('prod-007', 'Garden Tool Set', 'Complete set of essential garden tools', 89.99, 89.99, NULL, 'Home & Garden', NULL, 'Garden Pro', 'Tool Set', 'GARDEN-TOOLSET-COMPLETE', 40, 8, 2.5, 'Various sizes', '/images/products/garden-tools.jpg', 4.4, 67, false, true, NOW(), NOW()),
('prod-008', 'The Great Gatsby', 'Classic novel by F. Scott Fitzgerald', 12.99, 12.99, NULL, 'Books', NULL, 'Scribner', 'Paperback', 'BOOK-GATSBY-PAPERBACK', 200, 50, 0.3, '5.2 x 0.8 x 8 inches', '/images/products/gatsby-book.jpg', 4.7, 234, false, true, NOW(), NOW()),
('prod-009', 'Wireless Bluetooth Headphones', 'Noise-cancelling wireless headphones', 149.99, 199.99, 25.00, 'Electronics', 'Audio', 'SoundMax', 'Bluetooth Pro', 'AUDIO-HEADPHONES-BT', 60, 12, 0.25, '180 x 180 x 80 mm', '/images/products/bluetooth-headphones.jpg', 4.6, 178, true, true, NOW(), NOW()),
('prod-010', 'Smart Watch Series 8', 'Advanced fitness and health tracking smartwatch', 399.99, 449.99, 11.11, 'Electronics', 'Wearables', 'TechWear', 'Smart Watch 8', 'WEARABLE-SMARTWATCH-8', 35, 7, 0.038, '41 x 35 x 10.7 mm', '/images/products/smartwatch.jpg', 4.8, 92, true, true, NOW(), NOW());

-- Update category product counts
UPDATE categories SET product_count = 6 WHERE id = 'cat-001';
UPDATE categories SET product_count = 2 WHERE id = 'cat-002';
UPDATE categories SET product_count = 2 WHERE id = 'cat-003';
UPDATE categories SET product_count = 2 WHERE id = 'cat-004';
UPDATE categories SET product_count = 1 WHERE id = 'cat-005';
UPDATE categories SET product_count = 1 WHERE id = 'cat-006';
UPDATE categories SET product_count = 1 WHERE id = 'cat-007';
UPDATE categories SET product_count = 1 WHERE id = 'cat-008';

-- Insert Carts
INSERT INTO carts (id, user_id, total_amount, item_count, status, created_at, updated_at) VALUES
('cart-001', 'user-001', 0.00, 0, 'ACTIVE', NOW(), NOW()),
('cart-002', 'user-002', 0.00, 0, 'ACTIVE', NOW(), NOW()),
('cart-003', 'user-003', 0.00, 0, 'ACTIVE', NOW(), NOW());

-- Insert Cart Items
INSERT INTO cart_items (id, cart_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('cartitem-001', 'cart-001', 'prod-001', 'iPhone 15 Pro', '/images/products/iphone15pro.jpg', 1, 999.99, 999.99, NOW(), NOW()),
('cartitem-002', 'cart-001', 'prod-005', 'Men''s Casual T-Shirt', '/images/products/mens-tshirt.jpg', 2, 24.99, 49.98, NOW(), NOW()),
('cartitem-003', 'cart-002', 'prod-003', 'MacBook Pro 14"', '/images/products/macbookpro14.jpg', 1, 1999.99, 1999.99, NOW(), NOW()),
('cartitem-004', 'cart-003', 'prod-006', 'Women''s Summer Dress', '/images/products/womens-dress.jpg', 1, 59.99, 59.99, NOW(), NOW());

-- Update cart totals
UPDATE carts SET total_amount = 1049.97, item_count = 2 WHERE id = 'cart-001';
UPDATE carts SET total_amount = 1999.99, item_count = 1 WHERE id = 'cart-002';
UPDATE carts SET total_amount = 59.99, item_count = 1 WHERE id = 'cart-003';

-- Insert Orders (MySQL date arithmetic)
INSERT INTO orders (id, order_number, user_id, subtotal, tax_amount, shipping_amount, total_amount, status, payment_status, payment_method, delivery_address, delivery_city, delivery_state, delivery_zip_code, delivery_country, delivery_phone, estimated_delivery_date, tracking_number, notes, created_at, updated_at) VALUES
('order-001', 'ORD-2024-001', 'user-001', 1049.97, 104.99, 15.00, 1169.96, 'CONFIRMED', 'PAID', 'CREDIT_CARD', '123 Main Street', 'New York', 'NY', '10001', 'USA', '+1-555-0101', DATE_ADD(NOW(), INTERVAL 5 DAY), 'TRK123456789', 'Please deliver during business hours', NOW(), NOW()),
('order-002', 'ORD-2024-002', 'user-002', 1999.99, 199.99, 25.00, 2224.98, 'SHIPPED', 'PAID', 'CREDIT_CARD', '456 Oak Avenue', 'Los Angeles', 'CA', '90210', 'USA', '+1-555-0102', DATE_ADD(NOW(), INTERVAL 3 DAY), 'TRK987654321', 'Signature required', NOW(), NOW()),
('order-003', 'ORD-2024-003', 'user-003', 59.99, 6.00, 10.00, 75.99, 'DELIVERED', 'PAID', 'DEBIT_CARD', '789 Pine Street', 'Chicago', 'IL', '60601', 'USA', '+1-555-0103', DATE_SUB(NOW(), INTERVAL 2 DAY), 'TRK456789123', 'Left with neighbor', NOW(), NOW());

-- Insert Order Items
INSERT INTO order_items (id, order_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('orderitem-001', 'order-001', 'prod-001', 'iPhone 15 Pro', '/images/products/iphone15pro.jpg', 1, 999.99, 999.99, NOW(), NOW()),
('orderitem-002', 'order-001', 'prod-005', 'Men''s Casual T-Shirt', '/images/products/mens-tshirt.jpg', 2, 24.99, 49.98, NOW(), NOW()),
('orderitem-003', 'order-002', 'prod-003', 'MacBook Pro 14"', '/images/products/macbookpro14.jpg', 1, 1999.99, 1999.99, NOW(), NOW()),
('orderitem-004', 'order-003', 'prod-006', 'Women''s Summer Dress', '/images/products/womens-dress.jpg', 1, 59.99, 59.99, NOW(), NOW());

-- Insert Payments
INSERT INTO payments (id, order_id, user_id, amount, payment_method, status, transaction_id, gateway_response, card_last_four, card_brand, card_expiry_month, card_expiry_year, billing_address, billing_city, billing_state, billing_zip_code, billing_country, processed_at, created_at, updated_at) VALUES
('payment-001', 'order-001', 'user-001', 1169.96, 'CREDIT_CARD', 'SUCCESSFUL', 'TXN123456789', 'Approved', '1234', 'VISA', 12, 2026, '123 Main Street', 'New York', 'NY', '10001', 'USA', NOW(), NOW(), NOW()),
('payment-002', 'order-002', 'user-002', 2224.98, 'CREDIT_CARD', 'SUCCESSFUL', 'TXN987654321', 'Approved', '5678', 'MASTERCARD', 8, 2025, '456 Oak Avenue', 'Los Angeles', 'CA', '90210', 'USA', NOW(), NOW(), NOW()),
('payment-003', 'order-003', 'user-003', 75.99, 'DEBIT_CARD', 'SUCCESSFUL', 'TXN456789123', 'Approved', '9012', 'VISA', 3, 2027, '789 Pine Street', 'Chicago', 'IL', '60601', 'USA', NOW(), NOW(), NOW());

-- Insert additional sample products for variety
INSERT INTO products (id, name, description, price, original_price, discount_percentage, category, sub_category, brand, model, sku, stock_quantity, min_stock_level, weight, dimensions, image_url, average_rating, review_count, featured, active, created_at, updated_at) VALUES
('prod-011', 'Gaming Mouse', 'High-precision gaming mouse with RGB lighting', 79.99, 99.99, 20.00, 'Electronics', 'Computer Accessories', 'GameTech', 'Pro Gaming Mouse', 'ACC-MOUSE-GAMING', 80, 15, 0.12, '125 x 68 x 38 mm', '/images/products/gaming-mouse.jpg', 4.5, 134, false, true, NOW(), NOW()),
('prod-012', 'Mechanical Keyboard', 'Premium mechanical keyboard with Cherry MX switches', 129.99, 129.99, NULL, 'Electronics', 'Computer Accessories', 'KeyMaster', 'Mechanical Pro', 'ACC-KEYBOARD-MECH', 45, 10, 0.95, '440 x 135 x 35 mm', '/images/products/mechanical-keyboard.jpg', 4.7, 89, false, true, NOW(), NOW()),
('prod-013', 'Yoga Mat', 'Non-slip yoga mat for home workouts', 34.99, 39.99, 12.50, 'Home & Garden', 'Fitness', 'FitLife', 'Premium Yoga Mat', 'FIT-YOGAMAT-PREMIUM', 120, 25, 1.2, '183 x 61 x 0.6 cm', '/images/products/yoga-mat.jpg', 4.4, 67, false, true, NOW(), NOW()),
('prod-014', 'Coffee Maker', 'Programmable coffee maker with thermal carafe', 89.99, 89.99, NULL, 'Home & Garden', 'Kitchen', 'BrewMaster', 'Coffee Pro', 'KITCH-COFFEE-PRO', 35, 8, 2.8, '30 x 25 x 40 cm', '/images/products/coffee-maker.jpg', 4.6, 156, false, true, NOW(), NOW()),
('prod-015', 'Running Shoes', 'Lightweight running shoes for daily training', 89.99, 119.99, 25.00, 'Clothing', 'Athletic Wear', 'RunFast', 'Speed Runner', 'ATHL-SHOES-RUNNING', 60, 12, 0.28, 'US 7-12', '/images/products/running-shoes.jpg', 4.5, 234, true, true, NOW(), NOW());

-- Update category product counts for new products
UPDATE categories SET product_count = product_count + 2 WHERE id = 'cat-001';
UPDATE categories SET product_count = product_count + 2 WHERE id = 'cat-007';
UPDATE categories SET product_count = product_count + 1 WHERE id = 'cat-004';

-- Insert more sample orders for demonstration (MySQL date arithmetic)
INSERT INTO orders (id, order_number, user_id, subtotal, tax_amount, shipping_amount, total_amount, status, payment_status, payment_method, delivery_address, delivery_city, delivery_state, delivery_zip_code, delivery_country, delivery_phone, estimated_delivery_date, tracking_number, notes, created_at, updated_at) VALUES
('order-004', 'ORD-2024-004', 'user-004', 89.99, 9.00, 10.00, 108.99, 'PROCESSING', 'AUTHORIZED', 'CREDIT_CARD', '321 Elm Street', 'Boston', 'MA', '02101', 'USA', '+1-555-0104', DATE_ADD(NOW(), INTERVAL 7 DAY), 'TRK789123456', 'Apartment building - call upon arrival', NOW(), NOW()),
('order-005', 'ORD-2024-005', 'user-001', 149.99, 15.00, 15.00, 179.99, 'PENDING', 'PENDING', 'DIGITAL_WALLET', '123 Main Street', 'New York', 'NY', '10001', 'USA', '+1-555-0101', DATE_ADD(NOW(), INTERVAL 10 DAY), NULL, 'Second order from this customer', NOW(), NOW());

-- Insert corresponding order items
INSERT INTO order_items (id, order_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('orderitem-005', 'order-004', 'prod-007', 'Garden Tool Set', '/images/products/garden-tools.jpg', 1, 89.99, 89.99, NOW(), NOW()),
('orderitem-006', 'order-005', 'prod-009', 'Wireless Bluetooth Headphones', '/images/products/bluetooth-headphones.jpg', 1, 149.99, 149.99, NOW(), NOW());

-- Insert corresponding payments
INSERT INTO payments (id, order_id, user_id, amount, payment_method, status, transaction_id, gateway_response, card_last_four, card_brand, card_expiry_month, card_expiry_year, billing_address, billing_city, billing_state, billing_zip_code, billing_country, processed_at, created_at, updated_at) VALUES
('payment-004', 'order-004', 'user-004', 108.99, 'CREDIT_CARD', 'PROCESSING', 'TXN789123456', 'Authorized', '3456', 'AMEX', 6, 2026, '321 Elm Street', 'Boston', 'MA', '02101', 'USA', NULL, NOW(), NOW()),
('payment-005', 'order-005', 'user-001', 179.99, 'DIGITAL_WALLET', 'PENDING', NULL, NULL, NULL, NULL, NULL, NULL, '123 Main Street', 'New York', 'NY', '10001', 'USA', NULL, NOW(), NOW());

-- Insert more cart items for demonstration
INSERT INTO cart_items (id, cart_id, product_id, product_name, product_image, quantity, unit_price, subtotal, created_at, updated_at) VALUES
('cartitem-005', 'cart-002', 'prod-011', 'Gaming Mouse', '/images/products/gaming-mouse.jpg', 1, 79.99, 79.99, NOW(), NOW()),
('cartitem-006', 'cart-003', 'prod-015', 'Running Shoes', '/images/products/running-shoes.jpg', 1, 89.99, 89.99, NOW(), NOW());

-- Update cart totals for new items
UPDATE carts SET total_amount = 2079.98, item_count = 2 WHERE id = 'cart-002';
UPDATE carts SET total_amount = 149.98, item_count = 2 WHERE id = 'cart-003';

-- Commit all changes
COMMIT;
