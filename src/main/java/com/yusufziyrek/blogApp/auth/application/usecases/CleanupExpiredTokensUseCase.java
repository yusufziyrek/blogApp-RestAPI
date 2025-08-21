package com.yusufziyrek.blogApp.auth.application.usecases;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CleanupExpiredTokensUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Async
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void execute() {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(1); // Delete tokens older than 1 day after expiration
            refreshTokenRepository.deleteByExpiresAtBefore(cutoffTime);
            log.info("Cleaned up expired refresh tokens older than {}", cutoffTime);
        } catch (Exception e) {
            log.error("Error during refresh token cleanup", e);
        }
    }

    // Manual cleanup method
    public void executeManual() {
        refreshTokenRepository.deleteExpiredTokens();
        log.info("Manual cleanup of expired refresh tokens completed");
    }
}
