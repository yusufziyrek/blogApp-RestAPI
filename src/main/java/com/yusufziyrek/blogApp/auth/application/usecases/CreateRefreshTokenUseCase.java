package com.yusufziyrek.blogApp.auth.application.usecases;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.user.domain.User;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh.max-tokens-per-user:5}")
    private int maxTokensPerUser;

    public RefreshToken execute(User user, String deviceInfo, String ipAddress) {
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
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);
        refreshToken.setIsRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }
}
