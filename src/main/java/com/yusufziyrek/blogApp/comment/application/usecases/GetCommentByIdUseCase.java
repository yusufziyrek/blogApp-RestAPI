package com.yusufziyrek.blogApp.comment.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCommentByIdUseCase {
    
    private final CommentRepository commentRepository;
    
    public Comment execute(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("Comment not found with id: " + commentId));
    }
}
