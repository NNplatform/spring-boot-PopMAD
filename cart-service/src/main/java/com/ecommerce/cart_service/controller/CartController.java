package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.JsonResponse;
import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<JsonResponse> addToCart(@PathVariable Long userId, @RequestBody CartRequest request) {
        JsonResponse response = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<JsonResponse> removeCart(@PathVariable Long userId, @RequestBody CartRequest request) {
        JsonResponse response = cartService.removeFromCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<JsonResponse> getCartsByUserId(@PathVariable Long userId) {
        JsonResponse response = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
