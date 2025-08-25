package com.yusufziyrek.blogApp.like.application.usecases;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

public class LikePostUseCase {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    
    public LikePostUseCase(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    
    public LikeDomain execute(Long userId, Long postId) {
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
            
            return savedLike;
        } catch (Exception e) {
            throw new LikeException("Failed to create like: " + e.getMessage());
        }
    }
}
