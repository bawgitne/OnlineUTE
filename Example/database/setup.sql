-- 1. Create Database
DROP DATABASE IF EXISTS sales_mis;
CREATE DATABASE sales_mis CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sales_mis;

-- 2. Create Customers Table
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150) UNIQUE,
    address VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 3. Create Products Table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(30) NOT NULL UNIQUE,
    product_name VARCHAR(150) NOT NULL,
    category VARCHAR(100),
    unit_price DECIMAL(12, 2) NOT NULL DEFAULT 0,
    stock_qty INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 4. Create Orders Table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(20) NOT NULL UNIQUE,
    order_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    total_amount DECIMAL(14, 2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    note VARCHAR(255),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. Create Order Details Table
CREATE TABLE order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    line_total DECIMAL(14, 2) NOT NULL,
    CONSTRAINT fk_detail_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 6. Insert Sample Data
INSERT INTO customers (customer_code, full_name, phone, email, address)
VALUES
('CUS001', 'Nguyễn Văn A', '0901234567', 'nva@example.com', '123 Nguyễn Trãi, Q1, HCM'),
('CUS002', 'Trần Thị B', '0912233445', 'ttb@example.com', '456 Lê Lợi, Q. Ninh Kiều, Cần Thơ'),
('CUS003', 'Lê Văn C', '0987654321', 'lvc@example.com', '789 Trần Hưng Đạo, Hà Nội');

INSERT INTO products (sku, product_name, category, unit_price, stock_qty)
VALUES
('IP16PM', 'iPhone 16 Pro Max 256GB', 'Smartphone', 34990000.00, 50),
('MACM3', 'MacBook Pro M3 14-inch', 'Laptop', 45000000.00, 20),
('AWU2', 'Apple Watch Ultra 2', 'Watch', 21000000.00, 30),
('AIRP4', 'AirPods 4', 'Accessories', 4500000.00, 100);
