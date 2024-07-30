package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<JsonResponse> checkout(@RequestParam Long userId) {
        JsonResponse response = orderService.createOrder(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<JsonResponse> getOrder(@PathVariable Long orderId) {
        JsonResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<JsonResponse> getUserOrders(@PathVariable Long userId) {
        JsonResponse response = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<JsonResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        JsonResponse response = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<JsonResponse> getUserOrders() {
        JsonResponse response =  orderService.findAllStatus();
        return ResponseEntity.ok(response);
    }
}