package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;

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
            .orElseThrow(() -> new LikeException(String.format(ErrorMessages.LIKE_NOT_FOUND_FOR_POST, userId, postId), HttpStatus.NOT_FOUND));
        
        try {
            // Clean Architecture: Load post domain to update like count
            if (like.getPostId() != null) {
                PostDomain post = postRepository.findById(like.getPostId())
                    .orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, like.getPostId()), HttpStatus.NOT_FOUND));
                post.decrementLikeCount();
                postRepository.save(post);
            }
            
            likeRepository.delete(like);
        } catch (Exception e) {
            throw new LikeException(String.format("Failed to remove like: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public void executeForComment(Long userId, Long commentId) {
        
        LikeDomain like = likeRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseThrow(() -> new LikeException(String.format(ErrorMessages.LIKE_NOT_FOUND_FOR_COMMENT, userId, commentId), HttpStatus.NOT_FOUND));
        
        try {
            // Clean Architecture: Load comment domain to update like count
            if (like.getCommentId() != null) {
                CommentDomain comment = commentRepository.findById(like.getCommentId())
                    .orElseThrow(() -> new CommentException(String.format(ErrorMessages.COMMENT_NOT_FOUND_BY_ID, like.getCommentId()), HttpStatus.NOT_FOUND));
                comment.decrementLikeCount();
                commentRepository.save(comment);
            }
            
            likeRepository.delete(like);
        } catch (Exception e) {
            throw new LikeException(String.format("Failed to remove like: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
