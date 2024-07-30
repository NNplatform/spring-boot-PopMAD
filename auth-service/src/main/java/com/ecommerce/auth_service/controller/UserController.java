package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.dto.LoginRequestForm;
import com.ecommerce.auth_service.service.UserService;
import com.ecommerce.auth_service.model.User;
import com.ecommerce.auth_service.dto.JsonResponse;
import com.ecommerce.auth_service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse> getUserById(@PathVariable Long id) {
        JsonResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponse> login(@RequestBody LoginRequestForm loginRequestForm) {
        JsonResponse response = userService.login(loginRequestForm.getEmail(), loginRequestForm.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonResponse> logout() {
        JsonResponse response = userService.logout();
        return ResponseEntity.ok(response);
    }
}