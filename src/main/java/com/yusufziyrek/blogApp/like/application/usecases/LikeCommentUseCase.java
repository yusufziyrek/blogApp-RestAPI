package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.Like;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.CommentException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class LikeCommentUseCase {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    
    public Like execute(Long userId, Long commentId) {
        
        // Check if user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("User not found with id: " + userId));
        
        // Check if comment exists
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("Comment not found with id: " + commentId));
        
        // Check if already liked
        if (likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new RuntimeException("User has already liked this comment");
        }
        
        // Create like
        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        like.setPost(null); // This is a comment like
        
        return likeRepository.save(like);
    }
}
