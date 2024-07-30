package com.ecommerce.cart_service.repository;

import com.ecommerce.cart_service.model.Cart;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CartRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_CART_BY_ID =
            "SELECT * FROM carts WHERE cart_id = :cartId";

    public Optional<Cart> findByCartId(Long cartId) {
        List<Cart> carts = entityManager.createNativeQuery(GET_CART_BY_ID, Cart.class)
                .setParameter("cartId", cartId)
                .getResultList();
        return carts.isEmpty() ? Optional.empty() : Optional.of(carts.get(0));
    }

    private static final String GET_CART_BY_USER_ID =
            "SELECT * FROM carts WHERE user_id = :userId";

    public Optional<Cart> findByUserId(Long userId) {
        List<Cart> carts = entityManager.createNativeQuery(GET_CART_BY_USER_ID, Cart.class)
                .setParameter("userId", userId)
                .getResultList();
        return carts.isEmpty() ? Optional.empty() : Optional.of(carts.get(0));
    }

    @Transactional
    public Cart save(Cart cart) {
        entityManager.persist(cart);
        return cart;
    }
}
