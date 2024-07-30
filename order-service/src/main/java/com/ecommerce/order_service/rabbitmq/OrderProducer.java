package com.ecommerce.order_service.rabbitmq;

import com.ecommerce.order_service.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OrderProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);


    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    @Autowired
    public OrderProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrder(Order order) {
        logger.info("sending to consumer");

        String orderMessage = convertOrderToJson(order);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("createdByProducer", true);

        Message message = MessageBuilder.withBody(orderMessage.getBytes())
                .andProperties(messageProperties)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }



    private String convertOrderToJson(Order order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert order to JSON", e);
        }
    }
}