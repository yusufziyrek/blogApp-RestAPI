package com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.UpdateCommentUseCase;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UpdateCommentUseCaseImpl implements UpdateCommentUseCase {
    
    private final CommentRepository commentRepository;
    
    @Override
    public CommentDomain execute(Long commentId, String newText, Long userId) {
        log.info("Updating comment ID: {} by user ID: {}", commentId, userId);
        
        // Find comment
        CommentDomain comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("CommentDomain not found"));
        
        // Check if user can edit this comment (domain logic)
        if (!comment.canBeEditedBy(userId)) {
            log.warn("Unauthorized comment update attempt by user ID: {} for comment ID: {}", userId, commentId);
            throw new CommentException("You are not authorized to edit this comment");
        }
        
        try {
            // Update using domain logic
            comment.updateText(newText);
            
            CommentDomain updatedComment = commentRepository.save(comment);
            log.info("CommentDomain updated successfully with ID: {}", commentId);
            return updatedComment;
        } catch (IllegalArgumentException e) {
            log.error("CommentDomain validation failed during update: {}", e.getMessage());
            throw new CommentException(e.getMessage());
        }
    }
}
