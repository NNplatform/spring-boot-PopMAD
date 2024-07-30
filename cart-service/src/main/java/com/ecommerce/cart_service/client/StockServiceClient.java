package com.ecommerce.cart_service.client;

import com.ecommerce.cart_service.dto.JsonResponse;
import com.ecommerce.cart_service.dto.StockRequest;
import com.ecommerce.cart_service.dto.StockResponse;
import com.ecommerce.cart_service.service.Deserializer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

@Component
public class StockServiceClient {
    private final RestTemplate restTemplate;
    private final String stockServiceUrl;
    private final Deserializer deserializer;

    @Autowired
    public StockServiceClient(RestTemplate restTemplate,
                              @Value("${stock.service.url}") String stockServiceUrl,
                              Deserializer deserializer) {
        this.restTemplate = restTemplate;
        this.stockServiceUrl = stockServiceUrl;
        this.deserializer = deserializer;
    }

    @CircuitBreaker(name = "stockService", fallbackMethod = "reserveStockFallback")
    public JsonResponse reserveStock(Long productId, int quantity) {
        String url = stockServiceUrl + "/stock/reserve";
        StockRequest request = new StockRequest(productId, quantity);
        HttpEntity<StockRequest> entity = new HttpEntity<>(request);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        String responseBody = responseEntity.getBody();
        return deserializeResponse(responseBody, JsonResponse.class);
    }

    @CircuitBreaker(name = "stockService", fallbackMethod = "releaseStockFallback")
    public JsonResponse releaseStock(Long productId, int quantity) {
        String url = stockServiceUrl + "/stock/release";
        StockRequest request = new StockRequest(productId, quantity);
        HttpEntity<StockRequest> entity = new HttpEntity<>(request);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        String responseBody = responseEntity.getBody();
        return deserializeResponse(responseBody, JsonResponse.class);
    }

    @CircuitBreaker(name = "stockService", fallbackMethod = "getStockFallback")
    public JsonResponse<StockResponse> getStock(Long productId) {
        String url = stockServiceUrl + "/stock/{productId}";
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("productId", productId);

        ResponseEntity<JsonResponse<StockResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<JsonResponse<StockResponse>>() {},
                uriVariables
        );

        JsonResponse<StockResponse> jsonResponse = responseEntity.getBody();

        if (jsonResponse == null || "-1".equals(jsonResponse.getCode())) {
            throw new RuntimeException("Error fetching stock for product " + productId);
        }

        return jsonResponse;
    }

    private <T> T deserializeResponse(String responseBody, Class<T> clazz) {
        return deserializer.deserialize(responseBody, clazz)
                .orElseThrow(() -> new RuntimeException("Failed to deserialize response"));
    }

    private JsonResponse reserveStockFallback(Long productId, int quantity, Throwable t) {
        return new JsonResponse<>("-1", "Error reserving stock for product " + productId, t.getMessage(), null);
    }

    private JsonResponse releaseStockFallback(Long productId, int quantity, Throwable t) {
        return new JsonResponse<>("-1", "Error releasing stock for product " + productId, t.getMessage(), null);
    }

    private JsonResponse getStockFallback(Long productId, Throwable t) {
        return new JsonResponse<>("-1", "Error fetching stock for product " + productId, t.getMessage(), null);
    }
}