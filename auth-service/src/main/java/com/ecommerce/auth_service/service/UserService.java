package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.JsonResponse;
import com.ecommerce.auth_service.dto.UserIdResponse;
import com.ecommerce.auth_service.exception.UserNotFoundException;
import com.ecommerce.auth_service.model.User;
import com.ecommerce.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public JsonResponse<UserIdResponse> getUserById(Long id) {
        User user = userRepository.findByUserId(id);
        UserIdResponse myResult = new UserIdResponse(user.getUsername(), user.getEmail());
        return new JsonResponse<>("0", "successful", null, myResult);
    }

    public JsonResponse<Void> login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user.getPassword().equals(password)) {
                userRepository.updateLastLogin(user.getId(), LocalDateTime.now());
                System.out.println("Login successful");
                return new JsonResponse<>("0", "Login successful");
            } else {
                System.out.println("Login failed");
                return new JsonResponse<>("-1", "Login failed");
            }
        } catch (UserNotFoundException e) {
            return new JsonResponse<>("-1", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse<>("-1", "An unexpected error occurred");
        }
    }

    public JsonResponse<Void> logout() {
        return new JsonResponse<>("0", "Logout successful");
    }
}