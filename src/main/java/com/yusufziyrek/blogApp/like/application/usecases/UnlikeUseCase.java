package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.Like;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UnlikeUseCase {
    
    private final LikeRepository likeRepository;
    
    public void executeForPost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new LikeException("Like not found for user " + userId + " and post " + postId));
        
        likeRepository.delete(like);
    }
    
    public void executeForComment(Long userId, Long commentId) {
        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseThrow(() -> new LikeException("Like not found for user " + userId + " and comment " + commentId));
        
        likeRepository.delete(like);
    }
}
