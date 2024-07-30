package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/result")
    public ResponseEntity<JsonResponse> getResultCustomer() {
        JsonResponse response =  adminService.adminResult();
        return ResponseEntity.ok(response);
    }
}