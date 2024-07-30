package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.service.ProductService;
import com.ecommerce.product_service.dto.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse> getProductById(@PathVariable Long id) {
        System.out.println("Received request for product ID: " + id);
        JsonResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<JsonResponse> getAllProducts() {
        JsonResponse response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

}