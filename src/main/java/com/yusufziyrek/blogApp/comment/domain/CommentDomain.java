package com.yusufziyrek.blogApp.comment.domain;

import java.time.LocalDateTime;

/**
 * Pure Domain Model for CommentDomain - No Framework Dependencies
 * Contains only business logic and domain rules
 */
public class CommentDomain {
    
    private Long id;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long postId; // post'a ID ile referans
    private Long userId; // user'a ID ile referans
    private int likeCount; // yorumun beğeni sayısı
    
    // Yeni yorum için constructor
    public CommentDomain(String text, Long postId, Long userId) {
        this.text = text;
        this.postId = postId;
        this.userId = userId;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        validateComment();
    }
    
    // Kalıcı veriden gelen yorum constructor
    public CommentDomain(Long id, String text, LocalDateTime createdDate, 
                        LocalDateTime updatedDate, Long postId, Long userId) {
        this.id = id;
        this.text = text;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.postId = postId;
        this.userId = userId;
    }
    
    // Varsayılan constructor
    public CommentDomain() {}
    
    // Domain iş metotları
    public void updateText(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("CommentDomain text cannot be empty");
        }
        this.text = newText;
        this.updatedDate = LocalDateTime.now();
    }
    
    public boolean canBeEditedBy(Long requestUserId) {
        return this.userId != null && this.userId.equals(requestUserId);
    }
    
    public boolean canBeDeletedBy(Long requestUserId) {
        return this.userId != null && this.userId.equals(requestUserId);
    }
    
    public boolean isRecentlyCreated() {
        return this.createdDate != null && 
               this.createdDate.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    public boolean hasBeenUpdated() {
        return this.updatedDate != null && 
               this.createdDate != null &&
               this.updatedDate.isAfter(this.createdDate);
    }
    
    // Domain doğrulama metotları
    private void validateComment() {
        validateText();
        validatePostId();
        validateUserId();
    }
    
    private void validateText() {
        if (this.text == null || this.text.trim().isEmpty()) {
            throw new IllegalArgumentException("CommentDomain text cannot be empty");
        }
        if (this.text.length() > 1000) {
            throw new IllegalArgumentException("CommentDomain text cannot exceed 1000 characters");
        }
    }
    
    private void validatePostId() {
        if (this.postId == null || this.postId <= 0) {
            throw new IllegalArgumentException("Valid post ID is required");
        }
    }
    
    private void validateUserId() {
        if (this.userId == null || this.userId <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }
    
    // Getter'lar
    public Long getId() { return id; }
    public String getText() { return text; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public Long getPostId() { return postId; }
    public Long getUserId() { return userId; }
    public int getLikeCount() { return likeCount; }
    
    // Beğeni yönetimi için iş metotları
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
    
    // Setter'lar (iç kullanım)
    public void setId(Long id) { this.id = id; }
    public void setText(String text) { 
        this.text = text;
        this.updatedDate = LocalDateTime.now();
    }
    public void setPostId(Long postId) { this.postId = postId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
}
