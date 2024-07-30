package com.ecommerce.product_service.exception;

public class OutOfStockException extends RuntimeException {
    private final String code;
    public OutOfStockException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
