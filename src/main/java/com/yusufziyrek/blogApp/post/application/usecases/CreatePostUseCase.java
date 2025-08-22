package com.yusufziyrek.blogApp.post.application.usecases;

import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.post.domain.PostDomain;

/**
 * Use Case Interface for Creating PostDomain
 * Clean Architecture - Application Layer
 */
public interface CreatePostUseCase {
    PostDomain execute(CreatePostRequest request, Long userId);
}
