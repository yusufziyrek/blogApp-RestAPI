package com.yusufziyrek.blogApp.auth.infrastructure.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.user.domain.User;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpaRefreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRefreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByTokenAndIsRevokedFalse(String token) {
        return jpaRefreshTokenRepository.findByTokenAndIsRevokedFalse(token);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefreshToken> findByUserAndIsRevokedFalse(User user) {
        return jpaRefreshTokenRepository.findByUserAndIsRevokedFalse(user);
    }

    @Override
    public void deleteByToken(String token) {
        jpaRefreshTokenRepository.deleteByToken(token);
    }

    @Override
    public void deleteByUser(User user) {
        jpaRefreshTokenRepository.deleteByUser(user);
    }

    @Override
    public void deleteExpiredTokens() {
        jpaRefreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    @Override
    public void revokeAllByUser(User user) {
        jpaRefreshTokenRepository.revokeAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public int countActiveTokensByUser(User user) {
        return jpaRefreshTokenRepository.countActiveTokensByUser(user, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefreshToken> findExpiredTokens() {
        return jpaRefreshTokenRepository.findExpiredTokens(LocalDateTime.now());
    }

    @Override
    public void deleteByExpiresAtBefore(LocalDateTime dateTime) {
        jpaRefreshTokenRepository.deleteByExpiresAtBefore(dateTime);
    }
}
