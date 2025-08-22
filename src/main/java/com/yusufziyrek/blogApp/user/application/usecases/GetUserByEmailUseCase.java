package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Getting UserDomain by Email
 * Clean Architecture - Application Layer
 */
public interface GetUserByEmailUseCase {
    UserResponse execute(String email);
}
