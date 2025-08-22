package com.yusufziyrek.blogApp.auth.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.ports.usecases.ValidateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.exception.AuthException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ValidateRefreshTokenUseCaseImpl implements ValidateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenDomain execute(String tokenValue) {
        log.info("Validating refresh token");
        RefreshTokenDomain refreshToken = refreshTokenRepository.findByTokenAndIsRevokedFalse(tokenValue)
            .orElseThrow(() -> {
                log.warn("Invalid or expired refresh token provided");
                return new AuthException("Invalid or expired refresh token");
            });

        if (!refreshToken.isActive()) {
            log.warn("Refresh token is expired or revoked for user: {}", refreshToken.getUser().getEmail());
            throw new AuthException("Refresh token is expired or revoked");
        }

        log.info("Refresh token validated successfully for user: {}", refreshToken.getUser().getEmail());
        return refreshToken;
    }
}
