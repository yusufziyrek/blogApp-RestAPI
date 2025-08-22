package com.yusufziyrek.blogApp.post.domain;

import java.time.LocalDateTime;

/**
 * Pure Domain Model for PostDomain - No Framework Dependencies
 * Contains only business logic and domain rules
 */
public class PostDomain {
    
    private Long id;
    private String title;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int commentCount;
    private int likeCount;
    private Long userId; // user'a ID ile referans veriyorum
    
    // Yeni post oluşturma constructor
    public PostDomain(String title, String text, Long userId) {
        this.title = title;
        this.text = text;
        this.userId = userId;
        this.commentCount = 0;
        this.likeCount = 0;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
    
    // Kalıcı veriden gelen post constructor
    public PostDomain(Long id, String title, String text, LocalDateTime createdDate,
                     LocalDateTime updatedDate, int commentCount, int likeCount, Long userId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.userId = userId;
    }
    
    // Varsayılan constructor
    public PostDomain() {}
    
    // Domain iş metotları
    public void publish() {
        validateTitle();
        validateContent();
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
    
    public void incrementCommentCount() {
        this.commentCount++;
        this.updatedDate = LocalDateTime.now();
    }
    
    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
            this.updatedDate = LocalDateTime.now();
        }
    }
    
    public void incrementLikeCount() {
        this.likeCount++;
        this.updatedDate = LocalDateTime.now();
    }
    
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
            this.updatedDate = LocalDateTime.now();
        }
    }
    
    public boolean canBeEditedBy(Long requestUserId) {
        return this.userId != null && this.userId.equals(requestUserId);
    }
    
    public void updateContent(String newTitle, String newText) {
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            this.title = newTitle;
            validateTitle();
        }
        if (newText != null && !newText.trim().isEmpty()) {
            this.text = newText;
            validateContent();
        }
        this.updatedDate = LocalDateTime.now();
    }
    
    // Domain doğrulama metotları
    private void validateTitle() {
        if (this.title == null || this.title.trim().isEmpty()) {
            throw new IllegalArgumentException("PostDomain title cannot be empty");
        }
        if (this.title.length() > 200) {
            throw new IllegalArgumentException("PostDomain title cannot exceed 200 characters");
        }
        if (this.title.length() < 3) {
            throw new IllegalArgumentException("PostDomain title must be at least 3 characters long");
        }
    }
    
    private void validateContent() {
        if (this.text == null || this.text.trim().isEmpty()) {
            throw new IllegalArgumentException("PostDomain content cannot be empty");
        }
        if (this.text.length() < 10) {
            throw new IllegalArgumentException("PostDomain content must be at least 10 characters long");
        }
    }
    
    public boolean hasValidTitle() {
        try {
            validateTitle();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public boolean hasValidContent() {
        try {
            validateContent();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public boolean isPublished() {
        return this.createdDate != null;
    }
    
    public boolean isRecentlyCreated() {
        return this.createdDate != null && 
               this.createdDate.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    public boolean hasComments() {
        return this.commentCount > 0;
    }
    
    public boolean hasLikes() {
        return this.likeCount > 0;
    }
    
    // Getter'lar
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public int getCommentCount() { return commentCount; }
    public int getLikeCount() { return likeCount; }
    public Long getUserId() { return userId; }
    
    // Setter'lar (iç kullanım)
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { 
        this.title = title;
        this.updatedDate = LocalDateTime.now();
    }
    public void setText(String text) { 
        this.text = text;
        this.updatedDate = LocalDateTime.now();
    }
    public void setUserId(Long userId) { this.userId = userId; }
}
