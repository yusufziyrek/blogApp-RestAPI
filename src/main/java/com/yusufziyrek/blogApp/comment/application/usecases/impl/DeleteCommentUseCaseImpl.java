package com.yusufziyrek.blogApp.comment.application.usecases.impl;

import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.DeleteCommentUseCase;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

public class DeleteCommentUseCaseImpl implements DeleteCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    
    public DeleteCommentUseCaseImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
    
    @Override
    public void execute(Long commentId, Long userId) {
        // Find comment
        CommentDomain comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("CommentDomain not found"));
        
        // Check if user can delete this comment (domain logic)
        if (!comment.canBeDeletedBy(userId)) {
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
            
        } catch (Exception e) {
            throw new CommentException("Failed to delete comment: " + e.getMessage());
        }
    }
}
