package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.request.CreateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Creating UserDomain
 * Clean Architecture - Application Layer
 */
public interface CreateUserUseCase {
    UserResponse execute(CreateUserRequest request);
}
