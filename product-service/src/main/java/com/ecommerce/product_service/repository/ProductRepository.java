package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_PRODUCT_BY_ID =
            "SELECT * FROM products WHERE product_id = :id";
    public Product findByProductId(Long id) {
        try {
            return (Product) entityManager.createNativeQuery(GET_PRODUCT_BY_ID, Product.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ProductNotFoundException("-1", "Product not found with ID: " + id);
        }
    }

    private static final String FIND_ALL =
            "SELECT * FROM products";
    public List<Product> findAll() {
        try {
            return entityManager.createNativeQuery(FIND_ALL, Product.class)
                    .getResultList();
        } catch (NoResultException e) {
            throw new ProductNotFoundException("-1", "Not Found Any Product");
        }
    }

    private static final String UPDATE_SOLDOUT_STATUS = "UPDATE products SET is_sold_out = :isSoldOut WHERE product_id = :productId";
    @Modifying
    @Transactional
    public void updateProductSoldOutStatus(Long productId, Boolean isSoldOut) {
        entityManager.createNativeQuery(UPDATE_SOLDOUT_STATUS)
                .setParameter("productId", productId)
                .setParameter("isSoldOut", isSoldOut)
                .executeUpdate();
    }

    private static final String UPDATE_PRODUCT_QUANTITY = "UPDATE products SET stock = :quantity WHERE product_id = :productId";
    @Modifying
    @Transactional
    public void updateProductQuantity(Long productId, Integer quantity) {
        entityManager.createNativeQuery(UPDATE_PRODUCT_QUANTITY)
                .setParameter("productId", productId)
                .setParameter("quantity", quantity)
                .executeUpdate();
    }

    @Transactional
    public Product save(Product product) {
        if (product.getProductId() == null) {
            entityManager.persist(product);
        } else {
            entityManager.merge(product);
        }
        return product;
    }
}
