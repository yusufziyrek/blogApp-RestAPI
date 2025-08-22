package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Getting UserDomain by Username or Email
 * Clean Architecture - Application Layer
 */
public interface GetUserByUsernameOrEmailUseCase {
    UserResponse execute(String usernameOrEmail);
}
