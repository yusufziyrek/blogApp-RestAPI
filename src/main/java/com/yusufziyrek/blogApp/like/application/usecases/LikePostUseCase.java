package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikePostUseCase {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    
    public LikeDomain execute(Long userId, Long postId) {
        log.info("Creating like for post ID: {} by user ID: {}", postId, userId);
        
    // Kullanıcı var mı kontrol et (şimdilik sadece varlık kontrolü)
        try {
            userRepository.findById(userId).orElseThrow(() -> new UserException("UserDomain not found with id: " + userId));
        } catch (Exception e) {
            throw new UserException("UserDomain not found with id: " + userId);
        }
        
    // Post var mı kontrol et ve beğeni sayısını güncellemek için yükle
        PostDomain post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException("PostDomain not found with id: " + postId));
        
    // Daha önce beğenilmiş mi kontrol et
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            log.warn("UserDomain ID: {} has already liked post ID: {}", userId, postId);
            throw new LikeException("UserDomain has already liked this post");
        }
        
        try {
            // Beğeni oluştur (sadece ID kullanıyorum)
            LikeDomain like = new LikeDomain();
            like.setUserId(userId);
            like.setPostId(postId);
            like.setCommentId(null); // Bu bir post beğenisi
            
            LikeDomain savedLike = likeRepository.save(like);
            
            // Postun beğeni sayısını domain mantığıyla güncelle
            post.incrementLikeCount();
            postRepository.save(post);
            
            log.info("LikeDomain created successfully with ID: {} for post ID: {}", savedLike.getId(), postId);
            return savedLike;
        } catch (Exception e) {
            log.error("Error creating like for post ID: {} by user ID: {}: {}", postId, userId, e.getMessage());
            throw new LikeException("Failed to create like: " + e.getMessage());
        }
    }
}
