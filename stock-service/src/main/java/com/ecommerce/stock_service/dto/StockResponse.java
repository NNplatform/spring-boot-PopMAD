package com.ecommerce.stock_service.dto;

public class StockResponse {
    private Long productId;
    private int totalBalance;

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