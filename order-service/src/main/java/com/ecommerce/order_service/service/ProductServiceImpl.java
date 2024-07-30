package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductServiceClient;
import com.ecommerce.order_service.dto.ProductByIdResponse;
import com.ecommerce.order_service.dto.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductServiceClient productServiceClient;

    @Override
    public ProductByIdResponse getProductById(Long productId) {
        JsonResponse<ProductByIdResponse> response = productServiceClient.getProductById(productId);
        return response.getResult();
    }

    @Override
    public boolean checkStockAvailability(Long productId, int quantity) {
        ProductByIdResponse product = getProductById(productId);
        return product.getStock() >= quantity;
    }

    @Override
    public void purchaseProduct(Long productId, int quantity) {
        JsonResponse<Void> response = productServiceClient.purchaseProduct(productId, quantity);
        return;
    }
}

