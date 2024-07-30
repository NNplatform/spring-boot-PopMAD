package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.model.User;
import com.ecommerce.auth_service.model.Role;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import com.ecommerce.auth_service.exception.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;



@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String GET_USER_BY_ID =
            "SELECT * FROM users WHERE id = :id";
    public User findByUserId(Long id) {
        try {
            return (User) entityManager.createNativeQuery(GET_USER_BY_ID, User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new UserNotFoundException("-1", "User not found with ID: " + id);
        }
    }

    private static final String GET_ROLES_BY_USER_ID =
            "SELECT r.* FROM roles r " +
                    "JOIN user_roles ur ON r.id = ur.role_id " +
                    "WHERE ur.user_id = :userId";

    public Role findRolesByUserId(Long userId) {
        return (Role) entityManager.createNativeQuery(GET_ROLES_BY_USER_ID, Role.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    private static final String GET_USER_BY_EMAIL =
            "SELECT * FROM users WHERE email = :email";
    public User findByEmail(String email) {
        try {
            return (User) entityManager.createNativeQuery(GET_USER_BY_EMAIL, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("-1", "User not found with email: " + email);
        }
    }

    private static final String UPDATE_LAST_LOGIN =
            "UPDATE users SET last_login = :newLastLogin WHERE id = :userId";
    @Transactional
    public void updateLastLogin(Long userId, LocalDateTime newLastLogin) {
        entityManager.createNativeQuery(UPDATE_LAST_LOGIN)
                .setParameter("userId", userId)
                .setParameter("newLastLogin", newLastLogin)
                .executeUpdate();
    }
}
