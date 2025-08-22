package com.yusufziyrek.blogApp.auth.infrastructure.usecases.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.ports.usecases.RevokeRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RevokeRefreshTokenUseCaseImpl implements RevokeRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void execute(String tokenValue) {
        log.info("Revoking refresh token");
        RefreshTokenDomain refreshToken = refreshTokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> {
                log.warn("Refresh token not found for revocation");
                return new AuthException("Refresh token not found");
            });

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token revoked successfully for user: {}", refreshToken.getUser().getEmail());
    }

    @Override
    public void executeByUser(String email) {
        log.info("Revoking all refresh tokens for user: {}", email);
        UserDomain user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("User not found for token revocation: {}", email);
                return new AuthException("User not found");
            });
        
        refreshTokenRepository.revokeAllByUser(user);
        log.info("All refresh tokens revoked successfully for user: {}", email);
    }
}
