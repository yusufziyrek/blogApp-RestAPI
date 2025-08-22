package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPostLikesUseCase {
    
    private final LikeRepository likeRepository;
    
    public Page<LikeDomain> execute(Long postId, Pageable pageable) {
        return likeRepository.findByPostId(postId, pageable);
    }
    
    public long getPostLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
