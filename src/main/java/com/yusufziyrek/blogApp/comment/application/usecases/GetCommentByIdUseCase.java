package com.yusufziyrek.blogApp.comment.application.usecases;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;

public interface GetCommentByIdUseCase {
    CommentDomain execute(Long commentId);
}
