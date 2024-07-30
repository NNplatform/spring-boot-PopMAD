package com.ecommerce.order_service.exception;

public class OrderNotFoundException extends RuntimeException {
    private final String code;

    public OrderNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public OrderNotFoundException(String message) {
        super(message);
        this.code = "-1"; // Default code or whatever is appropriate
    }

    public String getCode() {
        return code;
    }
}
