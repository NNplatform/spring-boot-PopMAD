CREATE INDEX IF NOT EXISTS idx_product_id ON products (product_id);
CREATE INDEX idx_wishlist_user ON wishlists(user_id);
CREATE INDEX idx_wishlist_items_product ON wishlist_items(product_id);