package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;

public class GetPostLikesUseCase {
    
    private final LikeRepository likeRepository;
    
    public GetPostLikesUseCase(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    
    public Page<LikeDomain> execute(Long postId, Pageable pageable) {
        return likeRepository.findByPostId(postId, pageable);
    }
    
    public long getPostLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
