package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;

public class GetCommentLikesUseCase {
    
    private final LikeRepository likeRepository;
    
    public GetCommentLikesUseCase(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    
    public Page<LikeDomain> execute(Long commentId, Pageable pageable) {
        return likeRepository.findByCommentId(commentId, pageable);
    }
    
    public long getCommentLikeCount(Long commentId) {
        return likeRepository.countByCommentId(commentId);
    }
}
