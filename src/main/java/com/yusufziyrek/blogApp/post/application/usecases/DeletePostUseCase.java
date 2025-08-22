package com.yusufziyrek.blogApp.post.application.usecases;

public interface DeletePostUseCase {
    void execute(Long postId, Long currentUserId);
}
