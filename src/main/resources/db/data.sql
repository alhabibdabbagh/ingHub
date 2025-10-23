-- Insert customers
MERGE INTO customers (id, username, password, role, name) KEY (username)
VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8L/z3hs3qN6d6JZvTg/Wv7aKvZEuW', 'ROLE_ADMIN', 'Admin User'),
       (2, 'alice', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8L/z3hs3qN6d6JZvTg/Wv7aKvZEuW', 'ROLE_CUSTOMER', 'Alice Customer'),
       (3, 'bob', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8L/z3hs3qN6d6JZvTg/Wv7aKvZEuW', 'ROLE_CUSTOMER', 'Bob Customer'),
       (4, 'cust1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J8L/z3hs3qN6d6JZvTg/Wv7aKvZEuW', 'ROLE_CUSTOMER', 'Test Customer');

-- Insert orders (customer_id is FK to customers.id)
MERGE INTO orders (id, customer_id, asset_name, order_side, size, price, status, create_date) KEY (id)
VALUES (1, 2, 'TRY', 'BUY', 100.000000, 1.100000, 'PENDING', TIMESTAMP '2025-01-01 10:00:00'),
       (2, 2, 'TRY', 'SELL', 10.000000, 150.500000, 'MATCHED', TIMESTAMP '2025-01-02 11:30:00'),
       (3, 3, 'TRY', 'BUY', 0.500000, 45000.00, 'PENDING', TIMESTAMP '2025-02-10 09:15:00'),
       (4, 3, 'TRY', 'SELL', 5.000000, 2500.00, 'CANCELED', TIMESTAMP '2025-03-05 14:20:00');

-- Insert TRY assets for each customer (only TRY asset, used for buying/selling)
MERGE INTO assets (id, customer_id, asset_name, size, usable_size) KEY (id)
VALUES (1, 1, 'TRY', 100000.000000, 100000.000000),  -- Admin TRY balance
       (2, 2, 'TRY', 50000.000000, 45000.000000),    -- Alice TRY balance
       (3, 3, 'TRY', 75000.000000, 50000.000000),    -- Bob TRY balance
       (4, 4, 'TRY', 10000.000000, 10000.000000);    -- Test Customer TRY balance

-- Ensure identity (auto-increment) counters continue after seeded IDs
ALTER TABLE customers ALTER COLUMN id RESTART WITH 5;
ALTER TABLE orders    ALTER COLUMN id RESTART WITH 5;
ALTER TABLE assets    ALTER COLUMN id RESTART WITH 5;
