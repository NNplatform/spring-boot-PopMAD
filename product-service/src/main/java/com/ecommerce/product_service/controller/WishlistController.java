package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.JsonResponse;
import com.ecommerce.product_service.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<JsonResponse> getAllWishlists() {
        JsonResponse response = wishlistService.getWishlistAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse> getWishlistById(@PathVariable Long userId) {
        JsonResponse response = wishlistService.getWishlistById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<JsonResponse> addToWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        JsonResponse response = wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<JsonResponse> removeFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        JsonResponse response = wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(response);
    }
}