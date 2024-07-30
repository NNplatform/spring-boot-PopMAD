package com.ecommerce.cart_service.dto;

public class ProductByIdResponse {
    private Long productId;
    private String name;
    private String desc;
    private Long price;
    private Boolean isSoldOut;
    private Integer stock;
    private Integer outOfStockAttempts;

    public ProductByIdResponse() { }

    public ProductByIdResponse(Long productId, String name, String desc
            , Long price, Boolean isSoldOut, Integer stock, Integer outOfStockAttempts) {
        this.productId = productId;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.isSoldOut = isSoldOut;
        this.stock = stock;
        this.outOfStockAttempts = outOfStockAttempts;
    }

    // Getters and setters

    public Integer getOutOfStockAttempts() {
        return outOfStockAttempts;
    }

    public void setOutOfStockAttempts(Integer outOfStockAttempts) {
        this.outOfStockAttempts = outOfStockAttempts;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Boolean getIsSoldOut() {
        return isSoldOut;
    }

    public void setIsSoldOut(Boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }
}

