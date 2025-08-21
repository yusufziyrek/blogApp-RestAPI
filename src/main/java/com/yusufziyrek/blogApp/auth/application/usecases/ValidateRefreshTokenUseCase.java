package com.yusufziyrek.blogApp.auth.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.shared.exception.AuthException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class ValidateRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken execute(String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndIsRevokedFalse(tokenValue)
            .orElseThrow(() -> new AuthException("Invalid or expired refresh token"));

        if (!refreshToken.isValid()) {
            throw new AuthException("Refresh token is expired or revoked");
        }

        return refreshToken;
    }
}
