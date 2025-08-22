package com.yusufziyrek.blogApp.comment.application.usecases;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;

public interface UpdateCommentUseCase {
    CommentDomain execute(Long commentId, String newText, Long userId);
}
