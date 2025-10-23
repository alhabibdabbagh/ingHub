-- SQL (H2) schema for your entities
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

-- Customers table
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    name VARCHAR(255)
);

-- Orders table with FK to customers
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    asset_name VARCHAR(255) NOT NULL,
    order_side VARCHAR(50) NOT NULL,
    size DECIMAL(19,6) NOT NULL,
    price DECIMAL(19,6) NOT NULL,
    status VARCHAR(50) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Assets table with FK to customers (each customer has TRY asset)
CREATE TABLE assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    asset_name VARCHAR(255) NOT NULL,
    size DECIMAL(19,6) NOT NULL,
    usable_size DECIMAL(19,6) NOT NULL,
    CONSTRAINT fk_asset_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_assets_customer ON assets(customer_id);
