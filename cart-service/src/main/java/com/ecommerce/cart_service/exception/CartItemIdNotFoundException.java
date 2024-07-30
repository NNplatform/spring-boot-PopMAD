package com.ecommerce.cart_service.exception;

public class CartItemIdNotFoundException extends RuntimeException {
    private final String code;

    public CartItemIdNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}