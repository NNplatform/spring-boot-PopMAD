CREATE INDEX IF NOT EXISTS idx_user_id ON carts (user_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON carts (created_at);

CREATE INDEX IF NOT EXISTS idx_cart_id ON cart_items (cart_id);
CREATE INDEX IF NOT EXISTS idx_product_id ON cart_items (product_id);
CREATE INDEX IF NOT EXISTS idx_added_at ON cart_items (added_at);
