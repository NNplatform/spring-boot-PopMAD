package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.JsonResponse;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

@Service
@Transactional
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @CacheEvict(value = {"oneProduct", "allProduct"}, allEntries = true)
    public JsonResponse<Void> attemptPurchase(Long productId, int quantity) {
        Product product = productRepository.findByProductId(productId);

        if (product == null) {
            throw new ProductNotFoundException("-1", "Product not found with id: " + productId);
        }

        logger.debug("Previous amount of productId {} is {} pieces", product.getProductId(), product.getStock());

        if (product.getStock() == 0) {
            productRepository.updateProductSoldOutStatus(product.getProductId(), true);
            product.setOutOfStockAttempts(product.getOutOfStockAttempts() + 1);
            productRepository.save(product);
            return new JsonResponse<>("-1", "Product " + product.getName() + "(" + product.getProductId() + ")" + " out of stock");
        }

        if (product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            logger.debug("minus result", product.getStock() - quantity);
            logger.debug("Saving product with ID {} and stock {}", product.getProductId(), product.getStock());
            productRepository.save(product);
            if (product.getStock() == 0) {
                productRepository.updateProductSoldOutStatus(product.getProductId(), true);
            }
            logger.debug("After update stock productId({}) balance {} pieces", productId, product.getStock());
            return new JsonResponse<>("0", "Update stock of " + product.getName() + "(" + product.getProductId() + ")" + " successful");
        } else {
            product.setOutOfStockAttempts(product.getOutOfStockAttempts() + 1);
            productRepository.save(product);
            return new JsonResponse<>("-1", "Insufficient stock for product ID:" + product.getProductId());
        }
    }

    @CacheEvict(value = {"oneProduct", "allProduct"}, allEntries = true)
    public JsonResponse<Void> updateProductQuantity(Long productId, int quantity) {
        productRepository.findByProductId(productId);
        productRepository.updateProductQuantity(productId, quantity);
        logger.debug("Amount of productId {} is {} pieces", productId, quantity);
        return new JsonResponse<>("0", "Update stock of " + productId + " successful");
    }
}
