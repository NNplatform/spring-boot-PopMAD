package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.dto.CartRequest;
import com.ecommerce.order_service.exception.CartNotFoundException;
import com.ecommerce.order_service.model.CartItem;
import com.ecommerce.order_service.service.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class CartServiceClient {

    private final RestTemplate restTemplate;
    private final String cartServiceUrl;
    private final Deserializer deserializer;

    private static final Logger logger = LoggerFactory.getLogger(CartServiceClient.class);

    @Autowired
    public CartServiceClient(RestTemplate restTemplate,
                             @Value("${cart.service.url}") String cartServiceUrl,
                             Deserializer deserializer) {
        this.restTemplate = restTemplate;
        this.cartServiceUrl = cartServiceUrl;
        this.deserializer = deserializer;
    }

    @CircuitBreaker(name = "cartService", fallbackMethod = "getCartsByUserIdFallback")
    public JsonResponse<List<CartItem>> getCartsByUserId(Long userId) {
        String url = cartServiceUrl + "/cart/user/{userId}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class,
                userId
        );

        String responseBody = responseEntity.getBody();
        logger.info("Response: ", responseBody);

        JsonResponse<List<CartItem>> jsonResponse = deserializeResponse(responseBody, new TypeReference<JsonResponse<List<CartItem>>>() {});
        logger.info("jsonResponse: ", jsonResponse);

        // Check if the response code indicates a failure
        if ("-1".equals(jsonResponse.getCode())) {
            // Throw an exception if the user does not have any carts
            throw new CartNotFoundException("-1", jsonResponse.getMessage());
        }
        else return jsonResponse;
    }

    @CircuitBreaker(name = "cartService", fallbackMethod = "clearUserCartsFallback")
    public JsonResponse<Void> clearUserCarts(Long userId, CartRequest request) {
        String url = cartServiceUrl + "/cart/{userId}";
        HttpEntity<CartRequest> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class,
                userId
        );

        System.out.println("Response: " + responseEntity.getBody());

        // Use TypeReference for the response
        JsonResponse<Void> jsonResponse = deserializeResponse(responseEntity.getBody(), new TypeReference<JsonResponse<Void>>() {});

        if ("-1".equals(jsonResponse.getCode())) {
            throw new CartNotFoundException(jsonResponse.getCode(), jsonResponse.getMessage());
        }
        return jsonResponse;
    }

    private <T> T deserializeResponse(String responseBody, TypeReference<T> typeReference) {
        return deserializer.deserialize(responseBody, typeReference)
                .orElseThrow(() -> new RuntimeException("Failed to deserialize response"));
    }

    private JsonResponse<List<CartItem>> getCartsByUserIdFallback(Long userId, Throwable t) {
        JsonResponse<List<CartItem>> response = new JsonResponse<>("1", "n Error fetching carts", t.getMessage(), Collections.emptyList());
        return response;
    }

    public JsonResponse<Void> clearUserCartsFallback(Long userId, CartRequest request, Throwable t) {
        logger.error("Error clearing user carts: {}", t.getMessage());
        return new JsonResponse<>("-1", "Failed to clear user carts", null, null);
    }
}