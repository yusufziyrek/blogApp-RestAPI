package com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCommentByIdUseCaseImpl implements GetCommentByIdUseCase {
    
    private final CommentRepository commentRepository;
    
    @Override
    public CommentDomain execute(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("CommentDomain not found with id: " + commentId));
    }
}
