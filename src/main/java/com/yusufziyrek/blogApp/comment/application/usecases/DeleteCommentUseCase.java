package com.yusufziyrek.blogApp.comment.application.usecases;

public interface DeleteCommentUseCase {
    void execute(Long commentId, Long userId);
}
