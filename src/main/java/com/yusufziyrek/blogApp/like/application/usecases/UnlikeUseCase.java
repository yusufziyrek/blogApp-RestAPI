package com.yusufziyrek.blogApp.like.application.usecases;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

public class UnlikeUseCase {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    
    public UnlikeUseCase(LikeRepository likeRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }
    
    public void executeForPost(Long userId, Long postId) {
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
        } catch (Exception e) {
            throw new LikeException("Failed to remove like: " + e.getMessage());
        }
    }
    
    public void executeForComment(Long userId, Long commentId) {
        
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
        } catch (Exception e) {
            throw new LikeException("Failed to remove like: " + e.getMessage());
        }
    }
}
