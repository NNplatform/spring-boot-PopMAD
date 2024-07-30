package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductByIdResponse;
import com.ecommerce.product_service.dto.JsonResponse;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private static final long CACHE_TTL = 2;

    @PostConstruct
    public void warmCache() {
        getAllProducts();
        logger.info("Cache warmed up");
    }

    @CacheEvict(value = "products", allEntries = true)
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void clearProductCache() {
        logger.info("Product cache cleared");
    }

    @Cacheable(value = "oneProduct", key = "#id")
    public JsonResponse<ProductByIdResponse> getProductById(Long id) {
        // Fetch the product from the database
        Product product = productRepository.findByProductId(id);
        if (product == null) {
            throw new ProductNotFoundException("-1", "Product not found with id: " + id);
        }

        boolean isSoldOut = product.getStock() == 0;
        productRepository.updateProductSoldOutStatus(product.getProductId(), isSoldOut);

        ProductByIdResponse myResult = new ProductByIdResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().longValue(),
                isSoldOut,
                product.getStock(),
                product.getOutOfStockAttempts()
        );

        return new JsonResponse<>("0", "successful", null, myResult);
    }

    @Cacheable(value = "allProduct", key = "'allProducts'")
    public JsonResponse<List<ProductByIdResponse>> getAllProducts() {
        // Retrieve all products from the repository
        List<Product> products = productRepository.findAll();

        List<ProductByIdResponse> myResults = products.stream()
                .sorted(Comparator.comparing(Product::getProductId))
                .map(product -> {
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
                })
                .collect(Collectors.toList());

        return new JsonResponse<>("0", "successful (from database)", null, myResults);
    }
}