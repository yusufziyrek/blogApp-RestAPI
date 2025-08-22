package com.yusufziyrek.blogApp.like.domain;

import java.time.LocalDateTime;

/**
 * Pure Domain Model for LikeDomain - No Framework Dependencies
 * Contains only business logic and domain rules
 */
public class LikeDomain {
    
    private Long id;
    private LocalDateTime createdDate;
    private Long userId; // user'a ID ile referans (entity yok)
    private Long postId; // post'a ID ile referans (comment like için null olabilir)
    private Long commentId; // comment'e ID ile referans (post like için null olabilir)
    
    // Yeni post beğenisi için constructor
    public LikeDomain(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = null;
        this.createdDate = LocalDateTime.now();
        validateLike();
    }
    
    // Yeni yorum beğenisi için constructor
    public LikeDomain(Long userId, Long postId, Long commentId) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.createdDate = LocalDateTime.now();
        validateLike();
    }
    
    // Kalıcı veriden gelen like için constructor
    public LikeDomain(Long id, Long userId, Long postId, Long commentId, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
    }
    
    // Varsayılan constructor
    public LikeDomain() {}
    
    // Domain iş metotları
    public boolean isPostLike() {
        return this.postId != null && this.commentId == null;
    }
    
    public boolean isCommentLike() {
        return this.commentId != null;
    }
    
    public boolean canBeRemovedBy(Long requestUserId) {
        return this.userId != null && this.userId.equals(requestUserId);
    }
    
    public boolean isRecentlyCreated() {
        return this.createdDate != null && 
               this.createdDate.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    // Domain doğrulama metotları
    private void validateLike() {
        validateUserId();
        validateTarget();
    }
    
    private void validateUserId() {
        if (this.userId == null || this.userId <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }
    
    private void validateTarget() {
    // Beğeni ya posta ya da yoruma ait olmalı; ikisi de ya da hiçbirine ait olmamalı
        if (this.postId == null && this.commentId == null) {
            throw new IllegalArgumentException("LikeDomain must target either a post or a comment");
        }
        if (this.postId != null && this.commentId != null && !this.postId.equals(0L)) {
            throw new IllegalArgumentException("LikeDomain cannot target both a post and a comment simultaneously");
        }
        if (this.postId != null && this.postId <= 0) {
            throw new IllegalArgumentException("Valid post ID is required");
        }
        if (this.commentId != null && this.commentId <= 0) {
            throw new IllegalArgumentException("Valid comment ID is required");
        }
    }
    
    // Getter'lar
    public Long getId() { return id; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public Long getUserId() { return userId; }
    public Long getPostId() { return postId; }
    public Long getCommentId() { return commentId; }
    
    // Setter'lar (iç kullanım)
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }
}
