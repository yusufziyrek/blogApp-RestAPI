package com.yusufziyrek.blogApp.comment.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdateCommentUseCase {
    
    private final CommentRepository commentRepository;
    
    public Comment execute(Long commentId, String newText, Long userId) {
        // Find comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("Comment not found"));
        
        // Check if user can edit this comment (domain logic)
        if (!comment.canBeEditedBy(userId)) {
            throw new CommentException("You are not authorized to edit this comment");
        }
        
        // Update using domain logic
        comment.updateText(newText);
        
        return commentRepository.save(comment);
    }
}
