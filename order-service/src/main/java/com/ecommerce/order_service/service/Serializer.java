package com.ecommerce.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Serializer {
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

    public Serializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
    }

    public <T> String serialize(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing object of type {}", object.getClass().getSimpleName(), e);
            throw new RuntimeException("Error serializing object of type " + object.getClass().getSimpleName(), e);
        }
    }
}
