package com.ecommerce.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.exception.DeserializationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class Deserializer {
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public Deserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> Optional<T> deserialize(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            logger.warn("Received empty JSON string for deserialization.");
            return Optional.empty();
        }

        try {
            logger.debug("Deserializing JSON: {}", jsonString);
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            if (List.class.isAssignableFrom(clazz)) {
                if (jsonNode.isArray()) {
                    TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {};
                    List<T> list = objectMapper.readValue(jsonString, typeReference);
                    return Optional.of((T) list); // Unsafe cast, consider handling this more safely
                } else {
                    logger.error("Expected JSON array but found: {}", jsonNode.getNodeType());
                    return Optional.empty();
                }
            } else {
                T object = objectMapper.readValue(jsonString, clazz);
                return Optional.of(object);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing JSON into type {}: {}", clazz.getSimpleName(), e.getMessage(), e);
            return Optional.empty();
        }
    }

    public <T> JsonResponse<T> deserializeJsonResponse(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            logger.warn("Received empty JSON string for deserialization.");
            return new JsonResponse<>("-1", "Empty response", null, null);
        }

        try {
            logger.debug("Deserializing JsonResponse JSON: {}", jsonString);
            JsonResponse<T> jsonResponse = objectMapper.readValue(
                    jsonString,
                    objectMapper.getTypeFactory().constructParametricType(JsonResponse.class, clazz)
            );
            if ("-1".equals(jsonResponse.getCode())) {
                logger.error("API Error: {}", jsonResponse.getMessage());
            }
            return jsonResponse;
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing JSON into JsonResponse: {}", e.getMessage(), e);
            throw new DeserializationException("Failed to deserialize JsonResponse", e);
        }
    }

    public <T> JsonResponse<List<T>> deserializeJsonResponseList(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            logger.warn("Received empty JSON string for deserialization.");
            return new JsonResponse<>("-1", "Empty response", null, Collections.emptyList());
        }

        try {
            logger.debug("Deserializing JsonResponse JSON: {}", jsonString);
            TypeReference<JsonResponse<List<T>>> typeReference = new TypeReference<>() {};
            JsonResponse<List<T>> jsonResponse = objectMapper.readValue(jsonString, typeReference);
            if ("-1".equals(jsonResponse.getCode())) {
                logger.error("API Error: {}", jsonResponse.getMessage());
            }
            return jsonResponse;
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing JSON into JsonResponse: {}", e.getMessage(), e);
            throw new DeserializationException("Failed to deserialize JsonResponse", e);
        }
    }

    public <T> Optional<T> deserialize(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || jsonString.isEmpty()) {
            logger.warn("Received empty JSON string for deserialization.");
            return Optional.empty();
        }

        try {
            T object = objectMapper.readValue(jsonString, typeReference);
            return Optional.of(object);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing JSON: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}