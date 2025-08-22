package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.request.UpdateUserRequest;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Updating UserDomain Profile
 * Clean Architecture - Application Layer
 */
public interface UpdateUserProfileUseCase {
    UserResponse execute(Long userId, UpdateUserRequest request);
}
