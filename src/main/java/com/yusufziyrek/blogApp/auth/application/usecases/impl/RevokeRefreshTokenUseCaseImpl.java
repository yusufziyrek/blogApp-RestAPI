package com.yusufziyrek.blogApp.auth.application.usecases.impl;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.usecases.RevokeRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.UserDomain;

public class RevokeRefreshTokenUseCaseImpl implements RevokeRefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public RevokeRefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(String tokenValue) {
        RefreshTokenDomain refreshToken = refreshTokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> {
                return new AuthException("Refresh token not found");
            });

        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void executeByUser(String email) {
        UserDomain user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                return new AuthException("User not found");
            });
        
        refreshTokenRepository.revokeAllByUser(user);
    }
}
