package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.Wishlist;
import com.ecommerce.order_service.exception.OrderNotFoundException;
import com.ecommerce.order_service.exception.CartNotFoundException;
import com.ecommerce.order_service.exception.ProductNotFoundException;
import com.ecommerce.order_service.client.CartServiceClient;
import com.ecommerce.order_service.client.ProductServiceClient;
import com.ecommerce.order_service.dto.AdminResult;
import com.ecommerce.order_service.dto.OrderStatusResponse;
import com.ecommerce.order_service.dto.ProductByIdResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.Cart;
import com.ecommerce.order_service.model.CartItem;
import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.rabbitmq.OrderProducer;
import com.ecommerce.order_service.service.Deserializer;
import com.ecommerce.order_service.service.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;


    @Autowired
    private Serializer serializer;

    @Autowired
    private Deserializer deserializer;

    public JsonResponse<AdminResult> adminResult() {
        List<Order> orders = orderRepository.findAll();

        JsonResponse<List<Wishlist>> jsonResponse = productServiceClient.getWishList();
        List<Wishlist> wishlist = jsonResponse.getResult();

        AdminResult result = new AdminResult();
        result.setTotalUser(Long.valueOf(0)); //mock
        result.setPendingUser(Long.valueOf(0)); //mock
        result.setOrderedUser(Long.valueOf(orders.size()));
        result.setAttemptDepletedProductUser(Long.valueOf(wishlist.size()));
        return new JsonResponse<>("0", "successful", null, result);
    }

}
