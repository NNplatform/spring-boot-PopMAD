package com.ecommerce.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse<T> {
    private String code;
    private String message;
    private String cause;
    private T result;

    public JsonResponse() {
        this.code = "0";
    }

    public JsonResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResponse(String code, String message, String cause, T result) {
        this.code = code;
        this.message = message;
        this.cause = cause;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
