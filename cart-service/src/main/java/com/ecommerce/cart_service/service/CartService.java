package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.exception.CartIdNotFoundException;
import com.ecommerce.cart_service.dto.JsonResponse;
import com.ecommerce.cart_service.dto.ProductByIdResponse;
import com.ecommerce.cart_service.dto.StockResponse;
import com.ecommerce.cart_service.client.StockServiceClient;
import com.ecommerce.cart_service.client.ProductServiceClient;
import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.model.Cart;
import com.ecommerce.cart_service.model.CartItem;
import com.ecommerce.cart_service.repository.CartRepository;
import com.ecommerce.cart_service.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.*;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;

@Service
@Transactional
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockServiceClient stockServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public JsonResponse<String> addToCart(Long userId, Long productId, int quantity) {
        //Reserve Stock
        //JsonResponse<StockResponse> jsonResponse = stockServiceClient.reserveStock(productId, quantity);

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        cart.addItem(new CartItem(productId, quantity));
        cartRepository.save(cart);

        logger.info("Product ID {} added to cart for user ID {}", productId, userId);
        return new JsonResponse<>("0", "successful", null, "Product added to cart");
    }

//    private boolean checkStockAvailability(Long productId, int quantity) {
//        JsonResponse<StockResponse> jsonResponse = stockServiceClient.getStock(productId);
//        StockResponse stock = jsonResponse.getResult();
//
//        JsonResponse<ProductByIdResponse> productResponse = productServiceClient.getProductById(productId);
//        ProductByIdResponse product = productResponse.getResult();
//
//        return ((stock.getTotalBalance() >= quantity) && (product.getStock() >= quantity));
//    }

    @Transactional
    public JsonResponse<String> removeFromCart(Long userId, Long productId, int quantity) {
        // Check if the user has a cart
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (!optionalCart.isPresent()) {
            return new JsonResponse<>("-1", "failed", null, "Cart not found for user ID: " + userId);
        }

        Cart cart = optionalCart.get();

        // Check if the item exists in the cart before attempting to remove it
        if (!cart.hasItem(productId)) {
            return new JsonResponse<>("-1", "failed", null, "Product ID " + productId + " not found in cart for user ID: " + userId);
        }

        // Verify that the userId, productId, and quantity match in the cart
        CartItem cartItem = cart.getCartItem(productId);
        if (cartItem == null || !cartItem.getProductId().equals(productId) || cartItem.getQuantity() != quantity) {
            return new JsonResponse<>("-1", "failed", null, "Invalid userId, productId, or quantity in the cart");
        }

        // Release stock for the product
        //JsonResponse<StockResponse> jsonResponse = stockServiceClient.releaseStock(productId, quantity);

        // Remove the item from the cart
        cart.removeItem(userId, productId, quantity);
        cartRepository.save(cart);

        logger.info("Product ID {} removed from cart for user ID {}", productId, userId);
        return new JsonResponse<>("0", "successful", null, "Product removed from cart successfully");
    }

    @Transactional
    public JsonResponse<List<CartItem>> getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartIdNotFoundException("-1", "User: " + userId + " does not have any cart"));
        return new JsonResponse<>("0", "successful", null, cart.getItems());
    }
}
