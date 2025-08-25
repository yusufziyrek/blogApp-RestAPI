package com.yusufziyrek.blogApp.auth.application.usecases.impl;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.usecases.CleanupExpiredTokensUseCase;

import java.time.LocalDateTime;

public class CleanupExpiredTokensUseCaseImpl implements CleanupExpiredTokensUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    
    public CleanupExpiredTokensUseCaseImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void execute() {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(1); // Delete tokens older than 1 day after expiration
            refreshTokenRepository.deleteByExpiresAtBefore(cutoffTime);
        } catch (Exception e) {
        }
    }

    // Manual cleanup method
    public void executeManual() {
        refreshTokenRepository.deleteExpiredTokens();
    }
}
