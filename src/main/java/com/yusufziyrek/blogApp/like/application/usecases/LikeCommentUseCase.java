package com.yusufziyrek.blogApp.like.application.usecases;

import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.LikeException;

public class LikeCommentUseCase {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    
    public LikeCommentUseCase(LikeRepository likeRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }
    
    public LikeDomain execute(Long userId, Long commentId) {
        // Kullanıcının varlığını kontrol et (şimdilik sadece var mı yok mu)
        // TODO: UserRepository'ye existsById ekle
        try {
            userRepository.findById(userId).orElseThrow(() -> new UserException("UserDomain not found with id: " + userId));
        } catch (Exception e) {
            throw new UserException("UserDomain not found with id: " + userId);
        }
        
        // Yorumun varlığını kontrol et ve beğeni sayısını güncellemek için yükle
        CommentDomain comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("CommentDomain not found with id: " + commentId));
        
        // Zaten beğenilmiş mi kontrol et
        if (likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new LikeException("UserDomain has already liked this comment");
        }
        
        try {
            // Beğeni oluştur (sadece ID kullanıyorum)
            LikeDomain like = new LikeDomain();
            like.setUserId(userId);
            like.setCommentId(commentId);
            like.setPostId(null); // Bu bir yorum beğenisi
            
            LikeDomain savedLike = likeRepository.save(like);
            
            // Yorumun beğeni sayısını domain mantığı ile güncelle
            comment.incrementLikeCount();
            commentRepository.save(comment);
            
            return savedLike;
        } catch (Exception e) {
            throw new LikeException("Failed to create like: " + e.getMessage());
        }
    }
}
