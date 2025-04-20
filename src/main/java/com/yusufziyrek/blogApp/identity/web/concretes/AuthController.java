package com.yusufziyrek.blogApp.identity.web.concretes;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.identity.dto.requests.LoginRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.AuthResponse;
import com.yusufziyrek.blogApp.identity.service.abstracts.IAuthService;
import com.yusufziyrek.blogApp.identity.service.concretes.RefreshTokenService;
import com.yusufziyrek.blogApp.identity.web.abstracts.IAuthController;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.exception.AuthException;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
public class AuthController implements IAuthController {

	private final IAuthService authService;
	private final RefreshTokenService refreshTokenService;
	private final JwtUtil jwtUtil;

	@Override
	public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request) {
		String response = authService.register(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
	}

	@Override
	public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
	}

	@Override
	public ResponseEntity<ApiResponse<String>> verifyAccount(@RequestParam("token") String token) {
		String result = authService.verifyAccount(token);
		return ResponseEntity.ok(new ApiResponse<>(true, "Account verification successful", result));
	}

	@Override
	public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(@RequestBody Map<String, String> request) {
		String requestRefreshToken = request.get("refreshToken");
		var optionalToken = refreshTokenService.findByToken(requestRefreshToken);
		if (optionalToken.isEmpty()) {
			throw new AuthException("Refresh token not found!");
		}
		var refreshToken = refreshTokenService.verifyExpiration(optionalToken.get());
		String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getEmail(),
				refreshToken.getUser().getId());
		Map<String, String> data = Map.of("accessToken", newAccessToken, "refreshToken", refreshToken.getToken());
		return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", data));
	}
}
