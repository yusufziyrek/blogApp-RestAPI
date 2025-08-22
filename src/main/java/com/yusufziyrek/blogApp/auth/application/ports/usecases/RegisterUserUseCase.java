package com.yusufziyrek.blogApp.auth.application.ports.usecases;

import com.yusufziyrek.blogApp.auth.dto.request.RegisterRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Port for User Registration
 * Clean Architecture - Application Layer
 */
public interface RegisterUserUseCase {
    
    /**
     * Registers a new user in the system
     * @param registerRequest the registration data
     * @return UserResponse with the created user information
     * @throws com.yusufziyrek.blogApp.shared.exception.UserException if username or email already exists
     */
    UserResponse execute(RegisterRequest registerRequest);
}
