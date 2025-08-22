package com.yusufziyrek.blogApp.user.application.usecases;

import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

/**
 * Use Case Interface for Getting All Users
 * Clean Architecture - Application Layer
 */
public interface GetAllUsersUseCase {
    PageResponse<UserResponse> execute(int page, int size);
}
