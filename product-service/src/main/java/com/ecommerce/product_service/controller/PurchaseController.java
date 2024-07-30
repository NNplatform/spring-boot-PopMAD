package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.JsonResponse;
import com.ecommerce.product_service.exception.OutOfStockException;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class); // Initialize logger statically

    @PostMapping("/purchase/{productId}")
    public ResponseEntity<JsonResponse> purchase(@PathVariable Long productId, @RequestParam int quantity) {
        logger.info("Received purchase request for product {} with quantity {}", productId, quantity);

        try {
            JsonResponse response = purchaseService.attemptPurchase(productId, quantity);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            logger.error("Product not found with ID: {}", productId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new JsonResponse("error", "Product ID: "+productId+" not found")
            );
        } catch (OutOfStockException e) {
            logger.error("Insufficient stock for product ID: {}", productId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new JsonResponse("error", "Out of stock")
            );
        }
    }

    @PostMapping("/{productId}")
    public ResponseEntity<JsonResponse> updateStock(@PathVariable Long productId, @RequestParam int quantity) {
        JsonResponse response = purchaseService.updateProductQuantity(productId, quantity);
        return ResponseEntity.ok(response);
    }
}
