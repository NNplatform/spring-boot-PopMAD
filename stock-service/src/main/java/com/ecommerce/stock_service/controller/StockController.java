package com.ecommerce.stock_service.controller;

import com.ecommerce.stock_service.dto.JsonResponse;
import com.ecommerce.stock_service.dto.StockRequest;
import com.ecommerce.stock_service.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PatchMapping("/{productId}")
    public ResponseEntity<JsonResponse> addStockQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        JsonResponse response = stockService.addStockQuantity(productId, quantity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<JsonResponse> getStock(@PathVariable Long productId) {
        JsonResponse response = stockService.getStock(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reserve")
    public ResponseEntity<JsonResponse> reserveStock(@RequestBody StockRequest request) {
        JsonResponse response = stockService.reserveStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/release")
    public ResponseEntity<JsonResponse> releaseStock(@RequestBody StockRequest request) {
        JsonResponse response = stockService.releaseStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(response);
    }
}
