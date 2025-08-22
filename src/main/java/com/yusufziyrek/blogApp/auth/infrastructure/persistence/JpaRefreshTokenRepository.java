package com.yusufziyrek.blogApp.auth.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    Optional<RefreshTokenEntity> findByToken(String token);
    
    // Token ile bul, iptal edilmemiş olanları getir
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.token = :token AND r.isRevoked = false")
    Optional<RefreshTokenEntity> findByTokenAndIsRevokedFalse(@Param("token") String token);
    
    // Kullanıcı ID'si ile bul, iptal edilmemiş olanlar
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.user.id = :userId AND r.isRevoked = false")
    List<RefreshTokenEntity> findByUserIdAndIsRevokedFalse(@Param("userId") Long userId);
    
    // Token ile sil
    void deleteByToken(String token);
    
    // Kullanıcı ID'si ile sil
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    // Kullanıcıya ait iptal edilmemiş token sayısını say
    @Query("SELECT COUNT(r) FROM RefreshTokenEntity r WHERE r.user.id = :userId AND r.isRevoked = false")
    Long countByUserIdAndIsRevokedFalse(@Param("userId") Long userId);
    
    // Belirli tarihten önce sona eren token'ları bul
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.expiresAt < :expiresAt")
    List<RefreshTokenEntity> findByExpiresAtBefore(@Param("expiresAt") LocalDateTime expiresAt);
    
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.user.id = :userId AND r.isRevoked = false")
    List<RefreshTokenEntity> findActiveTokensByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.user.id = :userId")
    List<RefreshTokenEntity> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.expiresAt < :now")
    List<RefreshTokenEntity> findExpiredTokens(@Param("now") LocalDateTime now);
    
    void deleteByExpiresAtBefore(LocalDateTime expirationTime);
}
