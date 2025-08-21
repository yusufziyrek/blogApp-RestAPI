package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.Like;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCommentLikesUseCase {
    
    private final LikeRepository likeRepository;
    
    public Page<Like> execute(Long commentId, Pageable pageable) {
        return likeRepository.findByCommentId(commentId, pageable);
    }
    
    public long getCommentLikeCount(Long commentId) {
        return likeRepository.countByCommentId(commentId);
    }
}
