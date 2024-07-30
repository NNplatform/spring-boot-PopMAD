package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.CartItem;
import java.util.List;
import java.util.stream.Collectors;

public interface CartService {
    List<CartItem> getCartItems(Long userId);
    void clearUserCarts(Long userId, List<CartItem> cartItems);
}

