package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.http.HttpStatus;

import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.shared.exception.ErrorMessages;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;

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
        // Kullanıcının varlığını kontrol et
        userRepository.findById(userId)
            .orElseThrow(() -> new UserException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, userId), HttpStatus.NOT_FOUND));
        
        // Post var mı kontrol et ve beğeni sayısını güncellemek için yükle
        PostDomain post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException(String.format(ErrorMessages.POST_NOT_FOUND_BY_ID, postId), HttpStatus.NOT_FOUND));
        
        // Daha önce beğenilmiş mi kontrol et
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new LikeException(ErrorMessages.LIKE_ALREADY_EXISTS_FOR_POST, HttpStatus.CONFLICT);
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
            throw new LikeException(String.format("Failed to create like: %s", e.getMessage()));
        }
    }
}
