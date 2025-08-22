package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UnlikeUseCase {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    
    public void executeForPost(Long userId, Long postId) {
        log.info("Removing like for post ID: {} by user ID: {}", postId, userId);
        
        LikeDomain like = likeRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new LikeException("LikeDomain not found for user " + userId + " and post " + postId));
        
        try {
            // Clean Architecture: Load post domain to update like count
            if (like.getPostId() != null) {
                PostDomain post = postRepository.findById(like.getPostId())
                    .orElseThrow(() -> new PostException("PostDomain not found with id: " + like.getPostId()));
                post.decrementLikeCount();
                postRepository.save(post);
            }
            
            likeRepository.delete(like);
            log.info("LikeDomain removed successfully for post ID: {} by user ID: {}", postId, userId);
        } catch (Exception e) {
            log.error("Error removing like for post ID: {} by user ID: {}: {}", postId, userId, e.getMessage());
            throw new LikeException("Failed to remove like: " + e.getMessage());
        }
    }
    
    public void executeForComment(Long userId, Long commentId) {
        log.info("Removing like for comment ID: {} by user ID: {}", commentId, userId);
        
        LikeDomain like = likeRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseThrow(() -> new LikeException("LikeDomain not found for user " + userId + " and comment " + commentId));
        
        try {
            // Clean Architecture: Load comment domain to update like count
            if (like.getCommentId() != null) {
                CommentDomain comment = commentRepository.findById(like.getCommentId())
                    .orElseThrow(() -> new CommentException("CommentDomain not found with id: " + like.getCommentId()));
                comment.decrementLikeCount();
                commentRepository.save(comment);
            }
            
            likeRepository.delete(like);
            log.info("LikeDomain removed successfully for comment ID: {} by user ID: {}", commentId, userId);
        } catch (Exception e) {
            log.error("Error removing like for comment ID: {} by user ID: {}: {}", commentId, userId, e.getMessage());
            throw new LikeException("Failed to remove like: " + e.getMessage());
        }
    }
}
