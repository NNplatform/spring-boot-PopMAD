package com.ecommerce.product_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Deserializer {
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public Deserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
    }

    public <T> Optional<Object> deserialize(String jsonString, Class<T> clazz) {
        try {
            logger.debug("Deserializing JSON: {}", jsonString);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            if (jsonNode.isArray()) {
                List<T> list = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
                return Optional.of(list);
            } else {
                T object = objectMapper.readValue(jsonString, clazz);
                return Optional.of(object);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing object of type {}", clazz.getSimpleName(), e);
            throw new RuntimeException("Error deserializing object of type " + clazz.getSimpleName(), e);
        }
    }
}
