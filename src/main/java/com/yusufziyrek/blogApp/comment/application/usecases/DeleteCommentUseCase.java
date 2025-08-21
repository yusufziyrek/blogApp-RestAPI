package com.yusufziyrek.blogApp.comment.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class DeleteCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    
    public void execute(Long commentId, Long userId) {
        // Find comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("Comment not found"));
        
        // Check if user can delete this comment (domain logic)
        if (!comment.canBeEditedBy(userId)) {
            throw new CommentException("You are not authorized to delete this comment");
        }
        
        // Get post to update comment count
        Post post = comment.getPost();
        
        // Delete comment
        commentRepository.deleteById(commentId);
        
        // Update post comment count using domain logic
        if (post != null) {
            post.decrementCommentCount();
            postRepository.save(post);
        }
    }
}
