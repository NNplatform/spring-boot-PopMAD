package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.rabbitmq.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerImpl implements OrderProducerService {

    @Autowired
    private OrderProducer orderProducer;

    @Override
    public void sendOrder(Order order) {
        try {
            orderProducer.sendOrder(order);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order to RabbitMQ", e);
        }
    }
}
