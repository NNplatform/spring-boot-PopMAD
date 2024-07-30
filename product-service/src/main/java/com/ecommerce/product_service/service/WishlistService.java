package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.JsonResponse;
import com.ecommerce.product_service.dto.ProductByIdResponse;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.model.Wishlist;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    public JsonResponse<List<Wishlist>> getWishlistAll() {
        List<Wishlist> wishlists = wishlistRepository.findAll();

        if (wishlists.isEmpty()) {
            return new JsonResponse<>("0", "No wishlists found", null, Collections.emptyList());
        }

        return new JsonResponse<>("0", "Wishlists retrieved successfully", null, wishlists);
    }


    public JsonResponse<List<ProductByIdResponse>> getWishlistById(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createWishlist(userId));

        List<ProductByIdResponse> wishlistItems = wishlist.getProducts().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

        return new JsonResponse<>("0", "Wishlist retrieved successfully", null, wishlistItems);
    }

    public JsonResponse<String> addToWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createWishlist(userId));

        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new ProductNotFoundException("-1", "Product not found with id: " + productId);
        }

        wishlist.getProducts().add(product);
        wishlistRepository.save(wishlist);

        return new JsonResponse<>("0", "Product added to wishlist successfully", null, null);
    }

    public JsonResponse<String> removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user: " + userId));

        // Check if the product is in the wishlist
        boolean productExists = wishlist.getProducts().stream()
                .anyMatch(product -> product.getProductId().equals(productId));

        if (!productExists) {
            return new JsonResponse<>("-1", "Product not found in wishlist", null, null);
        }

        // Remove the product from the wishlist
        wishlist.getProducts().removeIf(product -> product.getProductId().equals(productId));
        wishlistRepository.save(wishlist);

        return new JsonResponse<>("0", "Product removed from wishlist successfully", null, null);
    }


    private Wishlist createWishlist(Long userId) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        return wishlistRepository.save(wishlist);
    }

    private ProductByIdResponse mapToProductResponse(Product product) {
        boolean isSoldOut = product.getStock() == 0;
        return new ProductByIdResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().longValue(),
                isSoldOut,
                product.getStock(),
                product.getOutOfStockAttempts()
        );
    }
}