package com.yusufziyrek.blogApp.auth.domain;

import java.time.LocalDateTime;

/**
 * Pure Domain Model for RefreshTokenDomain - No Framework Dependencies
 * Contains only business logic and domain rules
 */
public class RefreshTokenDomain {
    
    private Long id;
    private String token;
    private Long userId; // Reference to user by ID
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private LocalDateTime createdDate;
    
    // Constructor for new refresh token creation
    public RefreshTokenDomain(String token, Long userId, LocalDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.isRevoked = false;
        this.createdDate = LocalDateTime.now();
        validateToken();
    }
    
    // Constructor for existing refresh token (from persistence)
    public RefreshTokenDomain(Long id, String token, Long userId, LocalDateTime expiresAt, 
                              Boolean isRevoked, LocalDateTime createdDate) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.isRevoked = isRevoked;
        this.createdDate = createdDate;
    }
    
    // Default constructor
    public RefreshTokenDomain() {}
    
    // Domain Business Methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
    
    public boolean isActive() {
        return !this.isRevoked && !isExpired();
    }
    
    public void revoke() {
        this.isRevoked = true;
    }
    
    public void extendExpiry(int daysToAdd) {
        if (this.isRevoked) {
            throw new IllegalStateException("Cannot extend expiry of revoked token");
        }
        this.expiresAt = this.expiresAt.plusDays(daysToAdd);
    }
    
    // Domain Validation
    private void validateToken() {
        validateTokenString();
        validateUserId();
        validateExpiresAt();
    }
    
    private void validateTokenString() {
        if (this.token == null || this.token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        if (this.token.length() < 10) {
            throw new IllegalArgumentException("Token must be at least 10 characters long");
        }
    }
    
    private void validateUserId() {
        if (this.userId == null || this.userId <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }
    
    private void validateExpiresAt() {
        if (this.expiresAt == null) {
            throw new IllegalArgumentException("Expiry date is required");
        }
        if (this.expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
    }
    
    // Getters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public Boolean getIsRevoked() { return isRevoked; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    
    // Setters (for internal use)
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setIsRevoked(Boolean isRevoked) { this.isRevoked = isRevoked; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    // Additional fields for backwards compatibility
    private String deviceInfo;
    private String ipAddress;
    
    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    // For use cases that expect UserDomain reference
    private com.yusufziyrek.blogApp.user.domain.UserDomain user;
    
    public com.yusufziyrek.blogApp.user.domain.UserDomain getUser() { return user; }
    public void setUser(com.yusufziyrek.blogApp.user.domain.UserDomain user) { 
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }
}
