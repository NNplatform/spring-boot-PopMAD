package com.ecommerce.cart_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // This will manage the serialization of CartItem
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Cart() {}

    public Cart(Long userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        Optional<CartItem> existingItem = items.stream()
                .filter(cartItem -> cartItem.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(item.getQuantity());
        } else {
            items.add(item);
            item.setCart(this);
        }
    }

    public void removeItem(Long userId, Long productId, int quantity) {
        if (!this.userId.equals(userId)) {
            throw new IllegalArgumentException("User ID does not match the cart owner.");
        }
        items.removeIf(item -> item.getProductId().equals(productId) && item.getQuantity() <= quantity);
        items.stream()
                .filter(item -> item.getProductId().equals(productId) && item.getQuantity() > quantity)
                .findFirst()
                .ifPresent(item -> item.setQuantity(item.getQuantity() - quantity));
    }

    public boolean hasItem(Long productId) {
        return items.stream().anyMatch(cartItem -> cartItem.getProductId().equals(productId));
    }

    public CartItem getCartItem(Long productId) {
        return items.stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElse(null); // Return null if the item is not found
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", items=" + items +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}