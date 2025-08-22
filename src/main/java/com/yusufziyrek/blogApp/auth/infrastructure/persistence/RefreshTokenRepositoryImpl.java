package com.yusufziyrek.blogApp.auth.infrastructure.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.auth.infrastructure.mappers.RefreshTokenMapper;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.entity.UserEntity;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.JpaUserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public RefreshTokenDomain save(RefreshTokenDomain refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        
    // Kullanıcı referansını yükle ve setle
        UserEntity user = jpaUserRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        entity.setUser(user);
        
        RefreshTokenEntity savedEntity = jpaRefreshTokenRepository.save(entity);
        return refreshTokenMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshTokenDomain> findByToken(String token) {
        return jpaRefreshTokenRepository.findByToken(token)
            .map(refreshTokenMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshTokenDomain> findByTokenAndIsRevokedFalse(String token) {
        return jpaRefreshTokenRepository.findByTokenAndIsRevokedFalse(token)
            .map(refreshTokenMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefreshTokenDomain> findByUserAndIsRevokedFalse(UserDomain user) {
        return jpaRefreshTokenRepository.findByUserIdAndIsRevokedFalse(user.getId())
            .stream()
            .map(refreshTokenMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByToken(String token) {
        jpaRefreshTokenRepository.deleteByToken(token);
    }

    @Override
    public void deleteByUser(UserDomain user) {
        jpaRefreshTokenRepository.deleteByUserId(user.getId());
    }

    @Override
    public void deleteExpiredTokens() {
        jpaRefreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    @Override
    public void revokeAllByUser(UserDomain user) {
        List<RefreshTokenEntity> tokens = jpaRefreshTokenRepository.findByUserIdAndIsRevokedFalse(user.getId());
        tokens.forEach(token -> token.setIsRevoked(true));
        jpaRefreshTokenRepository.saveAll(tokens);
    }

    @Override
    @Transactional(readOnly = true)
    public int countActiveTokensByUser(UserDomain user) {
        return Math.toIntExact(jpaRefreshTokenRepository.countByUserIdAndIsRevokedFalse(user.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefreshTokenDomain> findExpiredTokens() {
        return jpaRefreshTokenRepository.findByExpiresAtBefore(LocalDateTime.now())
            .stream()
            .map(refreshTokenMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefreshTokenDomain> findTokensExpiringBefore(LocalDateTime dateTime) {
        return jpaRefreshTokenRepository.findByExpiresAtBefore(dateTime)
            .stream()
            .map(refreshTokenMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByExpiresAtBefore(LocalDateTime dateTime) {
        jpaRefreshTokenRepository.deleteByExpiresAtBefore(dateTime);
    }
}
