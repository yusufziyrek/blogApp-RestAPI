package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Getting UserDomain by ID
 * Clean Architecture - Application Layer
 */
public interface GetUserByIdUseCase {
    UserResponse execute(Long userId);
}
