package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

public class GetCommentByIdUseCaseImpl implements GetCommentByIdUseCase {
    
    private final CommentRepository commentRepository;
    
    public GetCommentByIdUseCaseImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public CommentDomain execute(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("CommentDomain not found with id: " + commentId));
    }
}
