package com.ecommerce.order_service.rabbitmq;

import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import java.io.IOException;

@Service
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void consumeOrder(Message message) {
        try {
            // Convert message to Order object
            Order order = objectMapper.readValue(message.getBody(), Order.class);
            logger.info("Received order from queue: {}", order.getOrderId());

            // Check if the order was created by the producer
            if (message.getMessageProperties().getHeaders().containsKey("createdByProducer")) {
                // Process the order but avoid creating a new order
                processOrder(order);
            } else {
                // Create a new order
                orderService.createOrder(order.getUserId());
            }

        } catch (IOException e) {
            logger.error("Error deserializing order from queue", e);
        } catch (Exception e) {
            logger.error("Error processing order from queue", e);
            // Consider implementing a retry mechanism or sending to a dead letter queue
        }
    }

    private void processOrder(Order order) {
        logger.info("received from prducer");
        order.setStatus("Processed");
        orderRepository.save(order);
        notifyUser(order);

        logger.info("Order processed successfully: {}", order.getOrderId());
    }

    private void notifyUser(Order order) {
        logger.info("Your order with ID {} has been processed successfully.", order.getOrderId());
    }
}
