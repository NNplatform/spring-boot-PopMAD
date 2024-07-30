package com.ecommerce.stock_service.exception;

public class UpdateStockException extends RuntimeException {
    private final String code;
    public UpdateStockException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
