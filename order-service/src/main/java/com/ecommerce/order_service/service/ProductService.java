package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.ProductByIdResponse;

public interface ProductService {
    ProductByIdResponse getProductById(Long productId);
    boolean checkStockAvailability(Long productId, int quantity);
    public void purchaseProduct(Long productId, int quantity);
}