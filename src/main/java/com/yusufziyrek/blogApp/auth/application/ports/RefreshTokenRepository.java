package com.yusufziyrek.blogApp.auth.application.ports;

import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
    
    RefreshTokenDomain save(RefreshTokenDomain refreshToken);
    
    Optional<RefreshTokenDomain> findByToken(String token);
    
    Optional<RefreshTokenDomain> findByTokenAndIsRevokedFalse(String token);
    
    List<RefreshTokenDomain> findByUserAndIsRevokedFalse(UserDomain user);
    
    void deleteByToken(String token);
    
    void deleteByUser(UserDomain user);
    
    void deleteExpiredTokens();
    
    void revokeAllByUser(UserDomain user);
    
    int countActiveTokensByUser(UserDomain user);
    
    List<RefreshTokenDomain> findExpiredTokens();
    
    List<RefreshTokenDomain> findTokensExpiringBefore(LocalDateTime dateTime);
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
