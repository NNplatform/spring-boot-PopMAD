package com.ecommerce.cart_service.dto;

public class StockRequest {
    private Long productId;
    private int quantity;

    // No-argument constructor
    public StockRequest() {
    }

    // Constructor with parameters
    public StockRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}