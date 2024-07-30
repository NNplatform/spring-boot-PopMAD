package com.ecommerce.stock_service.repository;

import com.ecommerce.stock_service.exception.ProductNotFoundException;
import com.ecommerce.stock_service.model.Stock;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StockRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_STOCK_BY_ID =
            "SELECT * FROM stock WHERE product_id = :id";
    public Stock findByProductId(Long id) {
        try {
            return (Stock) entityManager.createNativeQuery(GET_STOCK_BY_ID, Stock.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new ProductNotFoundException("-1", "Product not found with ID: " + id);
        }
    }

    @Transactional
    public Stock save(Stock stock) {
        entityManager.persist(stock);
        return stock;
    }
}