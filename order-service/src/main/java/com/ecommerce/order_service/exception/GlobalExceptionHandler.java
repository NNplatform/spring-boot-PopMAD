package com.ecommerce.order_service.exception;

import com.ecommerce.order_service.dto.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleOrderNotFoundException(OrderNotFoundException ex) {
        JsonResponse<?> jsonResponse = new JsonResponse<>(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleCartNotFoundException(CartNotFoundException ex) {
        JsonResponse<?> jsonResponse = new JsonResponse<>(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }


    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseBody
    public ResponseEntity<JsonResponse<?>> handleProductNotFoundException(ProductNotFoundException ex) {
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
