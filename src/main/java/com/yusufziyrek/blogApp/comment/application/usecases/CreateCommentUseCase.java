package com.yusufziyrek.blogApp.comment.application.usecases;

import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;

/**
 * Use Case Interface for Creating CommentDomain
 * Clean Architecture - Application Layer
 */
public interface CreateCommentUseCase {
    CommentResponse execute(CreateCommentRequest request, Long userId);
}
