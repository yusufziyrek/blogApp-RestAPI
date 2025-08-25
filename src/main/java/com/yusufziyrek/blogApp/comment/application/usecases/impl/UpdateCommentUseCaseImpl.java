package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.UpdateCommentUseCase;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

public class UpdateCommentUseCaseImpl implements UpdateCommentUseCase {
    
    private final CommentRepository commentRepository;
    
    public UpdateCommentUseCaseImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    @Override
    public CommentDomain execute(Long commentId, String newText, Long userId) {
        // Find comment
        CommentDomain comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("CommentDomain not found"));
        
        // Check if user can edit this comment (domain logic)
        if (!comment.canBeEditedBy(userId)) {
            throw new CommentException("You are not authorized to edit this comment");
        }
        
        try {
            // Update using domain logic
            comment.updateText(newText);
            
            CommentDomain updatedComment = commentRepository.save(comment);
            return updatedComment;
        } catch (IllegalArgumentException e) {
            throw new CommentException(e.getMessage());
        }
    }
}
