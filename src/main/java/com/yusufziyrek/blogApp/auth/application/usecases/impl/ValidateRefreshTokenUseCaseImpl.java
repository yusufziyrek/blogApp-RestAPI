package com.yusufziyrek.blogApp.auth.application.usecases.impl;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.usecases.ValidateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.exception.AuthException;

public class ValidateRefreshTokenUseCaseImpl implements ValidateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    
    public ValidateRefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        
    }

    @Override
    public RefreshTokenDomain execute(String tokenValue) {
        RefreshTokenDomain refreshToken = refreshTokenRepository.findByTokenAndIsRevokedFalse(tokenValue)
            .orElseThrow(() -> {
                return new AuthException("Invalid or expired refresh token");
            });

        if (!refreshToken.isActive()) {
            throw new AuthException("Refresh token is expired or revoked");
        }

        return refreshToken;
    }
}
