package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.model.Order;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.order_service.exception.OrderNotFoundException;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_Order_BY_ID =
            "SELECT * FROM orders WHERE order_id = :orderId";

    public Optional<Order> findById(Long orderId) {
        try {
            Order order = (Order) entityManager.createNativeQuery(GET_Order_BY_ID, Order.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();
            return Optional.ofNullable(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private static final String FIND_BY_USERID_AND_STATUS =
            "SELECT * FROM orders WHERE user_id = :userId AND status = :status";

    public Order findByUserIdAndStatus(Long userId, String status) {
        try {
            return (Order) entityManager.createNativeQuery(FIND_BY_USERID_AND_STATUS, Order.class)
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Order save(Order order) {
        if (order.getOrderId() == null) {
            entityManager.persist(order);
        } else {
            entityManager.merge(order);
        }
        return order;
    }

    // New methods

    private static final String FIND_BY_USERID =
            "SELECT * FROM orders WHERE user_id = :userId";

    public List<Order> findByUserId(Long userId) {
        return entityManager.createNativeQuery(FIND_BY_USERID, Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    private static final String FIND_ALL =
            "SELECT * FROM orders";

    public List<Order> findAll() {
        return entityManager.createNativeQuery(FIND_ALL, Order.class)
                .getResultList();
    }

    @Transactional
    public Order update(Order order) {
        return entityManager.merge(order);
    }
}