package com.ecommerce.cart_service.repository;

import com.ecommerce.cart_service.exception.CartItemIdNotFoundException;
import com.ecommerce.cart_service.exception.CartIdNotFoundException;
import com.ecommerce.cart_service.model.CartItem;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class CartItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_ITEM_BY_ID =
            "SELECT * FROM cart_items WHERE cart_item_id = :itemId";
    public CartItem findByItemId(Long itemId) {
        try {
            return (CartItem) entityManager.createNativeQuery(GET_ITEM_BY_ID, CartItem.class)
                    .setParameter("itemId", itemId)
                    .getSingleResult();
        } catch (CartItemIdNotFoundException e) {
            e.printStackTrace();
            throw new CartItemIdNotFoundException("-1", "CartItem not found ");
        }
    }

    private static final String GET_ITEM_BY_CART_ID =
            "SELECT * FROM cart_items WHERE cart_id = :cartId";
    public CartItem findByUserId( Long cartId) {
        try {
            return (CartItem) entityManager.createNativeQuery(GET_ITEM_BY_CART_ID, CartItem.class)
                    .setParameter("cartId", cartId)
                    .getResultList();
        } catch (CartIdNotFoundException e) {
            e.printStackTrace();
            throw new CartIdNotFoundException("-1", "Cart not found ");
        }
    }


    @Transactional
    public CartItem save(CartItem cart) {
        entityManager.persist(cart);
        return cart;
    }
}