package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.JsonResponse;
import com.ecommerce.auth_service.dto.UserIdResponse;
import com.ecommerce.auth_service.dto.UserResponse;
import com.ecommerce.auth_service.exception.UserNotFoundException;
import com.ecommerce.auth_service.model.User;
import com.ecommerce.auth_service.model.Role;
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
        if (user == null) {
            throw new UserNotFoundException("-1","User not found");
        }
        UserIdResponse myResult = new UserIdResponse(user.getId(), user.getUsername(), user.getEmail());
        return new JsonResponse<>("0", "successful", null, myResult);
    }

    public JsonResponse<UserResponse> login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                userRepository.updateLastLogin(user.getId(), LocalDateTime.now());
                Role role = getRolesByUserId(user.getId());
                UserResponse myResult = new UserResponse(user.getId(), role.getName());

                return new JsonResponse<>("0", "successful", null, myResult);
            } else {
                return new JsonResponse<>("-1", "Login failed",null,null);
            }
        } catch (UserNotFoundException e) {
            return new JsonResponse<>("-1", e.getMessage(),null,null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResponse<>("-1", "An unexpected error occurred",null,null);
        }
    }

    private Role getRolesByUserId(Long userId) {
        try {
            Role role = userRepository.findRolesByUserId(userId);
            if (role == null) {
                throw new UserNotFoundException("-1","No roles found for user");
            }
            return role;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new UserNotFoundException("-1","An unexpected error occurred durling getRoleByUserId");
        }
    }

    public JsonResponse<Void> logout() {
        return new JsonResponse<>("0", "Logout successful");
    }
}