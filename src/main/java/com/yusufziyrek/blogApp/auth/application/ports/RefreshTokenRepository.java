package com.yusufziyrek.blogApp.auth.application.ports;

import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
    
    RefreshToken save(RefreshToken refreshToken);
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByTokenAndIsRevokedFalse(String token);
    
    List<RefreshToken> findByUserAndIsRevokedFalse(User user);
    
    void deleteByToken(String token);
    
    void deleteByUser(User user);
    
    void deleteExpiredTokens();
    
    void revokeAllByUser(User user);
    
    int countActiveTokensByUser(User user);
    
    List<RefreshToken> findExpiredTokens();
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
