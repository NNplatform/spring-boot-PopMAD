CREATE TABLE IF NOT EXISTS stock (
    stock_id INT PRIMARY KEY,
    product_id INT,
    product_name VARCHAR(100),
    category VARCHAR(50),
    quantity INT,
    price DECIMAL(10, 2),
    supplier VARCHAR(100),
    received_date DATE,
    reorder_level INT,
    location VARCHAR(50)
);
