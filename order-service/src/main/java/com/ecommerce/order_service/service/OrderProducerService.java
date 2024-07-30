package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.Order;

public interface OrderProducerService {
    void sendOrder(Order order);
}
