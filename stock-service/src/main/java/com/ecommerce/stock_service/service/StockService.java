package com.ecommerce.stock_service.service;

import com.ecommerce.stock_service.exception.UpdateStockException;
import com.ecommerce.stock_service.dto.JsonResponse;
import com.ecommerce.stock_service.dto.StockResponse;
import com.ecommerce.stock_service.model.Stock;
import com.ecommerce.stock_service.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import org.springframework.scheduling.annotation.Scheduled;


@Service
@Transactional
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private StockRepository stockRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public JsonResponse<StockResponse> getStock(Long productId) {
        Stock stock = stockRepository.findByProductId(productId);
        StockResponse myResult = new StockResponse(stock.getProductId(), stock.getQuantity());
        return new JsonResponse<>("0", "successful", null, myResult);
    }

    @Transactional
    public JsonResponse<Void> reserveStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId);
        logger.debug("before reserve: "+ stock.getQuantity());
        if (stock.getQuantity() >= quantity) {
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);
            logger.debug("after release: "+ stock.getQuantity());
            return new JsonResponse<>("0", "Reservse temporary stock of produtcID:" + stock.getProductId()+" successful");
        }
        else {
            return new JsonResponse<>("-1", "Insufficient stock");
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public JsonResponse<Void> releaseStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId);
        if (stock == null) {
            return new JsonResponse<>("-1", "Stock not found for productId: " + productId);
        }

        int beforeQuantity = stock.getQuantity();
        logger.debug("Before release: " + beforeQuantity);

        stock.setQuantity(beforeQuantity + quantity);
        stockRepository.save(stock);
        entityManager.flush();
        entityManager.clear();

        Stock updatedStock = stockRepository.findByProductId(productId);
        logger.debug("After release: " + updatedStock.getQuantity());

        if (updatedStock.getQuantity() != beforeQuantity + quantity) {
            throw new RuntimeException("Stock update failed");
        }

        return new JsonResponse<>("0", "Release amount of productId:" + stock.getProductId() + " successful");
    }

    @Transactional
    public JsonResponse<Void> addStockQuantity(Long productId, int quantityToAdd) {
        Stock stock = stockRepository.findByProductId(productId);
        logger.debug("before update: "+ stock.getQuantity());
        int currentQuantity = stock.getQuantity();
        int newQuantity = currentQuantity + quantityToAdd;

        stock.setQuantity(newQuantity);
        stockRepository.save(stock);
        logger.debug("total after update: "+ stock.getQuantity());
        Stock updatedStock = stockRepository.findByProductId(productId);
        if (updatedStock.getQuantity() != newQuantity) {
            throw new UpdateStockException("-1", "Failed to update stock quantity");
        }

        return new JsonResponse<>("0", "Stock updated successfully for productId: " + productId);
    }
}
