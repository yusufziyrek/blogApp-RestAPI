package com.yusufziyrek.blogApp.auth.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByTokenAndIsRevokedFalse(String token);
    
    List<RefreshToken> findByUserAndIsRevokedFalse(User user);
    
    void deleteByToken(String token);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user = :user AND rt.isRevoked = false")
    void revokeAllByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user AND rt.isRevoked = false AND rt.expiresAt > :now")
    int countActiveTokensByUser(@Param("user") User user, @Param("now") LocalDateTime now);
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.expiresAt < :now")
    List<RefreshToken> findExpiredTokens(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :dateTime")
    void deleteByExpiresAtBefore(@Param("dateTime") LocalDateTime dateTime);
}
