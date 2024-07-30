package com.ecommerce.cart_service.client;

import com.ecommerce.cart_service.dto.JsonResponse;
import com.ecommerce.cart_service.dto.ProductByIdResponse;
import com.ecommerce.cart_service.exception.ProductNotFoundException;
import com.ecommerce.cart_service.service.Deserializer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

@Component
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;
    private final Deserializer deserializer;

    @Autowired
    public ProductServiceClient(RestTemplate restTemplate,
                                @Value("${product.service.url}") String productServiceUrl,
                                Deserializer deserializer) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
        this.deserializer = deserializer;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductByIdFallback")
    public JsonResponse<ProductByIdResponse> getProductById(Long productId) {
        String url = productServiceUrl + "/product/{productId}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class,
                productId
        );

        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        return deserializeResponse(responseBody, new TypeReference<JsonResponse<ProductByIdResponse>>() {});
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getAllProductsFallback")
    public JsonResponse getAllProducts() {
        String url = productServiceUrl + "/product";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );

        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        return deserializeResponse(responseBody, new TypeReference<JsonResponse<Object>>() {});
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "updateStockFallback")
    public JsonResponse updateProductStock(Long productId, int quantity) {
        String url = productServiceUrl + "/product/{productId}?quantity={quantity}";

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("productId", productId);
        uriVariables.put("quantity", quantity);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                String.class,
                uriVariables
        );

        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        return deserializeResponse(responseBody, new TypeReference<JsonResponse<Object>>() {});
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "purchaseProductFallback")
    public JsonResponse purchaseProduct(Long productId, int quantity) {
        String url = productServiceUrl + "/product/purchase/{productId}?quantity={quantity}";

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("productId", productId);
        uriVariables.put("quantity", quantity);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                String.class,
                uriVariables
        );

        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        return deserializeResponse(responseBody, new TypeReference<JsonResponse<Object>>() {});
    }

    private <T> JsonResponse<T> deserializeResponse(String responseBody, TypeReference<JsonResponse<T>> typeReference) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize response: " + e.getMessage(), e);
        }
    }

    private ResponseEntity<JsonResponse> getProductByIdFallback(Long productId, Throwable t) {
        JsonResponse response = new JsonResponse<>("-1", "Error fetching product", t.getMessage(), null);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<JsonResponse> getAllProductsFallback(Throwable t) {
        JsonResponse response = new JsonResponse<>("-1", "Error fetching all products", t.getMessage(), Collections.emptyList());
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<JsonResponse> updateStockFallback(Long productId, int quantity, Throwable t) {
        JsonResponse response = new JsonResponse<>("-1", "Error updating stock for product " + productId, t.getMessage(), null);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<JsonResponse> purchaseProductFallback(Long productId, int quantity, Throwable t) {
        JsonResponse response = new JsonResponse<>("-1", "Error purchasing product " + productId, t.getMessage(), null);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<JsonResponse> updateProductStockFallback(Long productId, int quantity, Throwable t) {
        JsonResponse response = new JsonResponse<>("-1", "Error updating product stock " + productId, t.getMessage(), null);
        return ResponseEntity.ok(response);
    }
}
