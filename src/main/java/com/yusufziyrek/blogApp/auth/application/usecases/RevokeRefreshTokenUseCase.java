package com.yusufziyrek.blogApp.auth.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.domain.RefreshToken;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class RevokeRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> new AuthException("Refresh token not found"));

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
    }

    public void executeAllByUser(User user) {
        refreshTokenRepository.revokeAllByUser(user);
    }
}
