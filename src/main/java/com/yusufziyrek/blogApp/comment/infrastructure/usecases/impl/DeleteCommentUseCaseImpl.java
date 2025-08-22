package com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.DeleteCommentUseCase;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeleteCommentUseCaseImpl implements DeleteCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    
    @Override
    public void execute(Long commentId, Long userId) {
        log.info("Deleting comment ID: {} by user ID: {}", commentId, userId);
        
        // Find comment
        CommentDomain comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("CommentDomain not found"));
        
        // Check if user can delete this comment (domain logic)
        if (!comment.canBeDeletedBy(userId)) {
            log.warn("Unauthorized comment deletion attempt by user ID: {} for comment ID: {}", userId, commentId);
            throw new CommentException("You are not authorized to delete this comment");
        }
        
        // Get post to update comment count
        Long postId = comment.getPostId();
        
        try {
            // Delete comment
            commentRepository.deleteById(commentId);
            
            // Update post comment count using domain logic
            if (postId != null) {
                PostDomain post = postRepository.findById(postId).orElse(null);
                if (post != null) {
                    post.decrementCommentCount();
                    postRepository.save(post);
                }
            }
            
            log.info("CommentDomain deleted successfully with ID: {}", commentId);
        } catch (Exception e) {
            log.error("Error deleting comment ID: {}: {}", commentId, e.getMessage());
            throw new CommentException("Failed to delete comment: " + e.getMessage());
        }
    }
}
