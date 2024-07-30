package com.ecommerce.order_service.exception;

public class ProductNotFoundException extends RuntimeException {
    private final String code;
    public ProductNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
