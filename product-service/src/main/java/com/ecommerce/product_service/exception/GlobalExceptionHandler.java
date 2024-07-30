package com.ecommerce.product_service.exception;

import com.ecommerce.product_service.dto.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleProductNotFoundException(ProductNotFoundException ex) {
        JsonResponse<?> jsonResponse = new JsonResponse<>(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleOutOfStockException(OutOfStockException ex) {
        JsonResponse<?> jsonResponse = new JsonResponse<>(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleGeneralException(Exception ex) {
        JsonResponse<?> jsonResponse = new JsonResponse<>("-1", "Unhandle Exception Occured: " + ex.getMessage());

        if ("-1".equals(jsonResponse.getCode()) || "-2".equals(jsonResponse.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
    }
}
