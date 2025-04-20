package com.yusufziyrek.blogApp.identity.service.concretes;

import com.yusufziyrek.blogApp.identity.domain.RefreshToken;
import com.yusufziyrek.blogApp.identity.domain.User;
import com.yusufziyrek.blogApp.identity.repo.IRefreshTokenRepository;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import com.yusufziyrek.blogApp.shared.exception.AuthException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	@Value("${jwt.refresh-token.expiration-ms}")
	private Long refreshTokenDurationMs;

	private final IRefreshTokenRepository refreshTokenRepository;
	private final IUserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AuthException("User not found with id: " + userId));
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(user);
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new AuthException("Refresh token expired. Please login again.");
		}
		return token;
	}

	public int deleteByUserId(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AuthException("User not found with id: " + userId));
		return refreshTokenRepository.deleteByUser(user);
	}
}
