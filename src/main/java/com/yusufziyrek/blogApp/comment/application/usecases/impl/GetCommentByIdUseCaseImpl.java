package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;

public class GetCommentByIdUseCaseImpl implements GetCommentByIdUseCase {
    
    private final CommentRepository commentRepository;
    
    public GetCommentByIdUseCaseImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public CommentDomain execute(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, commentId), HttpStatus.NOT_FOUND));
    }
}
