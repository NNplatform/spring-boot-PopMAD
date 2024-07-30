package com.ecommerce.order_service.service;

import com.ecommerce.order_service.model.CartItem;
import com.ecommerce.order_service.dto.JsonResponse;
import com.ecommerce.order_service.dto.CartRequest;
import com.ecommerce.order_service.client.CartServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private Serializer serializer;

    @Override
    public List<CartItem> getCartItems(Long userId) {
        JsonResponse<List<CartItem>> response = cartServiceClient.getCartsByUserId(userId);
        return response.getResult();
    }

    @Override
    public void clearUserCarts(Long userId, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            CartRequest request = new CartRequest(cartItem.getProductId(), cartItem.getQuantity());
            JsonResponse<?> response = cartServiceClient.clearUserCarts(userId, request);
            if (!"0".equals(response.getCode())) {
                throw new RuntimeException("Failed to clear user carts: " + response.getMessage());
            }
        }
    }
}

