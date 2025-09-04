-- =====================================================
-- ECOMMERCE BACKEND - SQLITE DDL SCRIPT
-- =====================================================
-- This script creates all tables for the ecommerce application
-- Compatible with SQLite 3.x
-- Generated from JPA Entity definitions
-- =====================================================

-- Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- =====================================================
-- 1. USERS TABLE
-- =====================================================
CREATE TABLE users (
    id TEXT PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    phone_number TEXT,
    role TEXT NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN', 'MODERATOR')),
    active INTEGER NOT NULL DEFAULT 1 CHECK (active IN (0, 1)),
    email_verified INTEGER NOT NULL DEFAULT 0 CHECK (email_verified IN (0, 1)),
    phone_verified INTEGER NOT NULL DEFAULT 0 CHECK (phone_verified IN (0, 1)),
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    last_login TEXT
);

-- =====================================================
-- 2. CATEGORIES TABLE
-- =====================================================
CREATE TABLE categories (
    id TEXT PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    description TEXT,
    parent_id TEXT,
    image_url TEXT,
    icon_class TEXT,
    sort_order INTEGER DEFAULT 0,
    product_count INTEGER DEFAULT 0,
    active INTEGER NOT NULL DEFAULT 1 CHECK (active IN (0, 1)),
    featured INTEGER NOT NULL DEFAULT 0 CHECK (featured IN (0, 1)),
    meta_title TEXT,
    meta_description TEXT,
    meta_keywords TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- =====================================================
-- 3. PRODUCTS TABLE
-- =====================================================
CREATE TABLE products (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    discount_percentage REAL,
    category TEXT NOT NULL,
    sub_category TEXT,
    brand TEXT,
    model TEXT,
    sku TEXT UNIQUE,
    stock_quantity INTEGER NOT NULL,
    min_stock_level INTEGER DEFAULT 10,
    weight REAL,
    dimensions TEXT,
    image_url TEXT,
    average_rating REAL DEFAULT 0.0,
    review_count INTEGER DEFAULT 0,
    featured INTEGER NOT NULL DEFAULT 0 CHECK (featured IN (0, 1)),
    active INTEGER NOT NULL DEFAULT 1 CHECK (active IN (0, 1)),
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- =====================================================
-- 4. PRODUCT_IMAGES TABLE (Collection Table)
-- =====================================================
CREATE TABLE product_images (
    product_id TEXT NOT NULL,
    image_url TEXT NOT NULL,
    PRIMARY KEY (product_id, image_url),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =====================================================
-- 5. CARTS TABLE
-- =====================================================
CREATE TABLE carts (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    item_count INTEGER DEFAULT 0,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CONVERTED', 'EXPIRED')),
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- 6. CART_ITEMS TABLE
-- =====================================================
CREATE TABLE cart_items (
    id TEXT PRIMARY KEY,
    cart_id TEXT NOT NULL,
    product_id TEXT NOT NULL,
    product_name TEXT NOT NULL,
    product_image TEXT,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =====================================================
-- 7. ORDERS TABLE
-- =====================================================
CREATE TABLE orders (
    id TEXT PRIMARY KEY,
    order_number TEXT UNIQUE NOT NULL,
    user_id TEXT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    shipping_amount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')),
    payment_status TEXT NOT NULL DEFAULT 'PENDING' CHECK (payment_status IN ('PENDING', 'AUTHORIZED', 'PAID', 'FAILED', 'REFUNDED', 'PARTIALLY_REFUNDED')),
    payment_method TEXT CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'DIGITAL_WALLET', 'CASH_ON_DELIVERY')),
    payment_transaction_id TEXT,
    delivery_address TEXT,
    delivery_city TEXT,
    delivery_state TEXT,
    delivery_zip_code TEXT,
    delivery_country TEXT,
    delivery_phone TEXT,
    estimated_delivery_date TEXT,
    tracking_number TEXT,
    notes TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- 8. ORDER_ITEMS TABLE
-- =====================================================
CREATE TABLE order_items (
    id TEXT PRIMARY KEY,
    order_id TEXT NOT NULL,
    product_id TEXT NOT NULL,
    product_name TEXT NOT NULL,
    product_image TEXT,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =====================================================
-- 9. PAYMENTS TABLE
-- =====================================================
CREATE TABLE payments (
    id TEXT PRIMARY KEY,
    order_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method TEXT NOT NULL CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'DIGITAL_WALLET', 'CASH_ON_DELIVERY')),
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'PROCESSED', 'SUCCESSFUL', 'FAILED', 'REFUNDED', 'PARTIALLY_REFUNDED', 'CANCELLED')),
    transaction_id TEXT UNIQUE,
    gateway_response TEXT,
    gateway_error_code TEXT,
    gateway_error_message TEXT,
    card_last_four TEXT,
    card_brand TEXT,
    card_expiry_month INTEGER,
    card_expiry_year INTEGER,
    billing_address TEXT,
    billing_city TEXT,
    billing_state TEXT,
    billing_zip_code TEXT,
    billing_country TEXT,
    processed_at TEXT,
    failure_reason TEXT,
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- 10. PASSWORD_RESET_TOKENS TABLE
-- =====================================================
CREATE TABLE password_reset_tokens (
    id TEXT PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    user_id TEXT NOT NULL,
    expiry_date TEXT NOT NULL,
    used INTEGER NOT NULL DEFAULT 0 CHECK (used IN (0, 1)),
    created_at TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Users table indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(active);

-- Categories table indexes
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_categories_active ON categories(active);
CREATE INDEX idx_categories_featured ON categories(featured);

-- Products table indexes
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_featured ON products(featured);
CREATE INDEX idx_products_stock_quantity ON products(stock_quantity);

-- Carts table indexes
CREATE INDEX idx_carts_user_id ON carts(user_id);
CREATE INDEX idx_carts_status ON carts(status);

-- Cart items table indexes
CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product_id ON cart_items(product_id);

-- Orders table indexes
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Order items table indexes
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

-- Payments table indexes
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);

-- Password reset tokens table indexes
CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_tokens_token ON password_reset_tokens(token);
CREATE INDEX idx_password_reset_tokens_expiry_date ON password_reset_tokens(expiry_date);

-- =====================================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- =====================================================

-- Trigger to update updated_at timestamp for users
CREATE TRIGGER update_users_timestamp 
    AFTER UPDATE ON users
    FOR EACH ROW
BEGIN
    UPDATE users SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for categories
CREATE TRIGGER update_categories_timestamp 
    AFTER UPDATE ON categories
    FOR EACH ROW
BEGIN
    UPDATE categories SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for products
CREATE TRIGGER update_products_timestamp 
    AFTER UPDATE ON products
    FOR EACH ROW
BEGIN
    UPDATE products SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for carts
CREATE TRIGGER update_carts_timestamp 
    AFTER UPDATE ON carts
    FOR EACH ROW
BEGIN
    UPDATE carts SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for cart_items
CREATE TRIGGER update_cart_items_timestamp 
    AFTER UPDATE ON cart_items
    FOR EACH ROW
BEGIN
    UPDATE cart_items SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for orders
CREATE TRIGGER update_orders_timestamp 
    AFTER UPDATE ON orders
    FOR EACH ROW
BEGIN
    UPDATE orders SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for order_items
CREATE TRIGGER update_order_items_timestamp 
    AFTER UPDATE ON order_items
    FOR EACH ROW
BEGIN
    UPDATE order_items SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- Trigger to update updated_at timestamp for payments
CREATE TRIGGER update_payments_timestamp 
    AFTER UPDATE ON payments
    FOR EACH ROW
BEGIN
    UPDATE payments SET updated_at = datetime('now') WHERE id = NEW.id;
END;

-- =====================================================
-- VIEWS FOR COMMON QUERIES
-- =====================================================

-- View for active products with category information
CREATE VIEW v_active_products AS
SELECT 
    p.id,
    p.name,
    p.description,
    p.price,
    p.original_price,
    p.discount_percentage,
    p.category,
    p.sub_category,
    p.brand,
    p.model,
    p.sku,
    p.stock_quantity,
    p.min_stock_level,
    p.image_url,
    p.average_rating,
    p.review_count,
    p.featured,
    p.created_at,
    p.updated_at,
    c.name as category_name,
    c.description as category_description
FROM products p
LEFT JOIN categories c ON p.category = c.name
WHERE p.active = 1;

-- View for user order summary
CREATE VIEW v_user_order_summary AS
SELECT 
    u.id as user_id,
    u.email,
    u.first_name,
    u.last_name,
    COUNT(o.id) as total_orders,
    SUM(o.total_amount) as total_spent,
    MAX(o.created_at) as last_order_date,
    MIN(o.created_at) as first_order_date
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.email, u.first_name, u.last_name;

-- View for product sales summary
CREATE VIEW v_product_sales_summary AS
SELECT 
    p.id as product_id,
    p.name as product_name,
    p.sku,
    p.category,
    p.price,
    SUM(oi.quantity) as total_sold,
    SUM(oi.subtotal) as total_revenue,
    COUNT(DISTINCT oi.order_id) as order_count,
    AVG(oi.unit_price) as avg_selling_price
FROM products p
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.id
WHERE o.status NOT IN ('CANCELLED', 'REFUNDED')
GROUP BY p.id, p.name, p.sku, p.category, p.price;

-- =====================================================
-- SAMPLE DATA INSERTION (Optional)
-- =====================================================

-- Insert sample categories
INSERT INTO categories (id, name, description, parent_id, image_url, icon_class, sort_order, product_count, active, featured, meta_title, meta_description, meta_keywords) VALUES
('cat-001', 'Electronics', 'Latest electronic gadgets and devices', NULL, '/images/categories/electronics.jpg', 'fas fa-mobile-alt', 1, 0, 1, 1, 'Electronics Store', 'Shop the latest electronics and gadgets', 'electronics, gadgets, devices, tech'),
('cat-002', 'Smartphones', 'Mobile phones and smartphones', 'cat-001', '/images/categories/smartphones.jpg', 'fas fa-mobile-alt', 1, 0, 1, 1, 'Smartphones', 'Latest smartphones and mobile phones', 'smartphones, mobile, phones, android, iphone'),
('cat-003', 'Laptops', 'Portable computers and laptops', 'cat-001', '/images/categories/laptops.jpg', 'fas fa-laptop', 2, 0, 1, 1, 'Laptops', 'High-performance laptops and computers', 'laptops, computers, portable, gaming, business'),
('cat-004', 'Clothing', 'Fashion and apparel for all ages', NULL, '/images/categories/clothing.jpg', 'fas fa-tshirt', 2, 0, 1, 1, 'Fashion Store', 'Trendy clothing and accessories', 'clothing, fashion, apparel, style'),
('cat-005', 'Books', 'Books, magazines, and educational materials', NULL, '/images/categories/books.jpg', 'fas fa-book', 4, 0, 1, 0, 'Bookstore', 'Wide selection of books and educational materials', 'books, reading, education, fiction, non-fiction');

-- Insert sample users
INSERT INTO users (id, email, password, first_name, last_name, phone_number, role, active, email_verified, phone_verified) VALUES
('user-001', 'john.doe@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Doe', '+1-555-0101', 'USER', 1, 1, 1),
('user-002', 'jane.smith@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', '+1-555-0102', 'USER', 1, 1, 0),
('user-003', 'admin@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', '+1-555-0000', 'ADMIN', 1, 1, 1);

-- Insert sample products
INSERT INTO products (id, name, description, price, original_price, discount_percentage, category, sub_category, brand, model, sku, stock_quantity, min_stock_level, weight, dimensions, image_url, average_rating, review_count, featured, active) VALUES
('prod-001', 'iPhone 15 Pro', 'Latest iPhone with advanced camera system and A17 Pro chip', 999.99, 1099.99, 9.09, 'Electronics', 'Smartphones', 'Apple', 'iPhone 15 Pro', 'IPH15PRO-128', 50, 10, 0.187, '146.7 x 71.5 x 8.25 mm', '/images/products/iphone15pro.jpg', 4.8, 125, 1, 1),
('prod-002', 'Samsung Galaxy S24', 'Flagship Android smartphone with AI features', 899.99, 899.99, NULL, 'Electronics', 'Smartphones', 'Samsung', 'Galaxy S24', 'SAMS24-256', 45, 10, 0.168, '147.0 x 70.6 x 7.6 mm', '/images/products/galaxys24.jpg', 4.6, 89, 1, 1),
('prod-003', 'MacBook Pro 14"', 'Professional laptop with M3 Pro chip', 1999.99, 2199.99, 9.09, 'Electronics', 'Laptops', 'Apple', 'MacBook Pro 14"', 'MBP14-M3PRO-512', 25, 5, 1.6, '312.6 x 221.2 x 15.5 mm', '/images/products/macbookpro14.jpg', 4.9, 67, 1, 1),
('prod-004', 'The Great Gatsby', 'Classic novel by F. Scott Fitzgerald', 12.99, 12.99, NULL, 'Books', NULL, 'Scribner', 'Paperback', 'BOOK-GATSBY-PAPERBACK', 200, 50, 0.3, '5.2 x 0.8 x 8 inches', '/images/products/gatsby-book.jpg', 4.7, 234, 0, 1);

-- Update category product counts
UPDATE categories SET product_count = 3 WHERE id = 'cat-001';
UPDATE categories SET product_count = 2 WHERE id = 'cat-002';
UPDATE categories SET product_count = 1 WHERE id = 'cat-003';
UPDATE categories SET product_count = 1 WHERE id = 'cat-005';

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Verify table creation
SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;

-- Verify indexes creation
SELECT name FROM sqlite_master WHERE type='index' AND name NOT LIKE 'sqlite_%' ORDER BY name;

-- Verify triggers creation
SELECT name FROM sqlite_master WHERE type='trigger' ORDER BY name;

-- Verify views creation
SELECT name FROM sqlite_master WHERE type='view' ORDER BY name;

-- =====================================================
-- END OF DDL SCRIPT
-- =====================================================
