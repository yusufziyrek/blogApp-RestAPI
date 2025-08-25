package com.yusufziyrek.blogApp.auth.application.usecases.impl;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.usecases.CreateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

import java.time.LocalDateTime;

public class CreateRefreshTokenUseCaseImpl implements CreateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final int maxTokensPerUser;
    
    public CreateRefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, int maxTokensPerUser) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.maxTokensPerUser = maxTokensPerUser;
    }

    @Override
    public RefreshTokenDomain execute(UserDomain user, String deviceInfo, String ipAddress) {
        
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
        return savedToken;
    }
}

