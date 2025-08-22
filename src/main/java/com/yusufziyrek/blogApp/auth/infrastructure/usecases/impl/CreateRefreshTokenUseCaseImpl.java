package com.yusufziyrek.blogApp.auth.infrastructure.usecases.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.ports.usecases.CreateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateRefreshTokenUseCaseImpl implements CreateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh.max-tokens-per-user:5}")
    private int maxTokensPerUser;

    @Override
    public RefreshTokenDomain execute(UserDomain user, String deviceInfo, String ipAddress) {
        log.info("Creating refresh token for user: {}", user.getEmail());
        
        // Clean up expired tokens first
        refreshTokenRepository.deleteExpiredTokens();
        
        // Check if user has too many active tokens
        int activeTokenCount = refreshTokenRepository.countActiveTokensByUser(user);
        if (activeTokenCount >= maxTokensPerUser) {
            // Revoke oldest tokens to make room
            var activeTokens = refreshTokenRepository.findByUserAndIsRevokedFalse(user);
            activeTokens.stream()
                .sorted((a, b) -> a.getCreatedDate().compareTo(b.getCreatedDate()))
                .limit(activeTokenCount - maxTokensPerUser + 1)
                .forEach(token -> {
                    token.revoke();
                    refreshTokenRepository.save(token);
                });
        }

        // Generate new refresh token
        String tokenValue = jwtUtil.generateRefreshToken(user.getEmail());
        
        // Calculate expiration time (30 days)
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);

        // Create and save refresh token entity
        RefreshTokenDomain refreshToken = new RefreshTokenDomain();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);
        refreshToken.setIsRevoked(false);

        RefreshTokenDomain savedToken = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created successfully for user: {}", user.getEmail());
        return savedToken;
    }
}
