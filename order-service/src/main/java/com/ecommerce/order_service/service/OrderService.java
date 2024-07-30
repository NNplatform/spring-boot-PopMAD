package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderStatusResponse;
import com.ecommerce.order_service.dto.ProductByIdResponse;
import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.exception.OrderNotFoundException;
import com.ecommerce.order_service.exception.ProductNotFoundException;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.Cart;
import com.ecommerce.order_service.model.CartItem;
import com.ecommerce.order_service.repository.OrderRepository;
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
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProducerService orderProducerService;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final ThreadLocal<Boolean> isConsumerOrder = ThreadLocal.withInitial(() -> false);

    @Transactional
    public JsonResponse<Order> createOrderFromCart(Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            return new JsonResponse<>("-1", "No carts found for user ID: " + userId, null, null);
        }

        Order order = createOrder(cartItems, userId);
        orderRepository.save(order);
        cartService.clearUserCarts(userId, cartItems);

        if (!isOrderCreatedByConsumer()) {
            orderProducerService.sendOrder(order);
        }

        return new JsonResponse<>("0", "Order created successfully", null, order);
    }

    private Order createOrder(List<CartItem> cartItems, Long userId) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductByIdResponse product = productService.getProductById(cartItem.getProductId());

            if (!productService.checkStockAvailability(cartItem.getProductId(), cartItem.getQuantity())) {
                throw new ProductNotFoundException("-1", "Insufficient stock for product ID: " + cartItem.getProductId());
            }

            if (product == null) {
                throw new ProductNotFoundException("-1", "Product not found for ID: " + cartItem.getProductId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            orderItem.setCartId(cartItem.getCartId());
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            productService.purchaseProduct(orderItem.getProductId(), orderItem.getQuantity());
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setTotalAmount(totalAmount);

        logger.debug("orderItems class: ", orderItems.getClass());

        // Set the order for each OrderItem
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        order.setOrderItems(orderItems);

        return order;
    }

    private boolean isOrderCreatedByConsumer() {
        return isConsumerOrder.get();
    }

    public JsonResponse<Order> createOrder(Long userId) {
        try {
            isConsumerOrder.set(true);
            return createOrderFromCart(userId);
        } finally {
            isConsumerOrder.remove();
        }
    }

    public JsonResponse<List<OrderItem>> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(o -> new JsonResponse<>("0", "successful", null, o.getOrderItems()))
                .orElseGet(() -> new JsonResponse<>("1", "Order not found with id: " + orderId, null, null));
    }

    public JsonResponse<List<OrderStatusResponse>> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderStatusResponse> result = orders.stream()
                .map(order -> new OrderStatusResponse(order.getOrderId(), order.getStatus()))
                .collect(Collectors.toList());
        return new JsonResponse<>("0", "successful", null, result);
    }

    @Transactional
    public JsonResponse<Void> updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("-1", "Order not found with id: " + orderId));
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        try {
            order = orderRepository.save(order);
            logger.info("Order saved with ID: {}", order.getOrderId());
        } catch (Exception e) {
            logger.error("Failed to save order", e);
            throw e;
        }
        return new JsonResponse<>("0", "successful update order status from " + order.getStatus() + " to " + newStatus, null, null);
    }

    public JsonResponse<List<OrderStatusResponse>> findAllStatus() {
        List<Order> orders = orderRepository.findAll();
        List<OrderStatusResponse> result = orders.stream()
                .map(order -> new OrderStatusResponse(order.getOrderId(), order.getStatus()))
                .collect(Collectors.toList());
        return new JsonResponse<>("0", "successful", null, result);
    }
}
