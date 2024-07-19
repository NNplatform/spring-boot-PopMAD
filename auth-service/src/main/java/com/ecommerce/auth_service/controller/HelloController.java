package com.ecommerce.auth_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}