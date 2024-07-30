package com.ecommerce.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockResponse {

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("totalBalance")
    private int totalBalance;

    // Default no-argument constructor
    public StockResponse() {
    }

    // Parameterized constructor
    public StockResponse(Long productId, int totalBalance) {
        this.productId = productId;
        this.totalBalance = totalBalance;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }
}
