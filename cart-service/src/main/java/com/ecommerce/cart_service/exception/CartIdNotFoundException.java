package com.ecommerce.cart_service.exception;

public class CartIdNotFoundException extends RuntimeException {
    private final String code;
    private final String message;

    public CartIdNotFoundException(String code, String message) {
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