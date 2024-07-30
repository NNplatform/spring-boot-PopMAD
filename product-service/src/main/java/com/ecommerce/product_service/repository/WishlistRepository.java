package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.model.Wishlist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class WishlistRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_ALL =
            "SELECT w FROM Wishlist w";

    public List<Wishlist> findAll() {
        return entityManager.createQuery(GET_ALL, Wishlist.class)
                .getResultList();
    }

    private static final String GET_BY_USER_ID =
            "SELECT w FROM Wishlist w LEFT JOIN FETCH w.products WHERE w.userId = :userId";

    public Optional<Wishlist> findByUserId(Long userId) {
        try {
            Wishlist wishlist = entityManager.createQuery(GET_BY_USER_ID, Wishlist.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.ofNullable(wishlist);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Wishlist save(Wishlist wishlist) {
        if (wishlist.getId() == null) {
            entityManager.persist(wishlist);
        } else {
            wishlist = entityManager.merge(wishlist);
        }
        return wishlist;
    }
}
