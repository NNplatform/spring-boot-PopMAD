package com.ecommerce.order_service.exception;

public class CartNotFoundException extends RuntimeException {
    private final String code;
    private final String message;

    public CartNotFoundException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}