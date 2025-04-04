package com.yusufziyrek.blogApp.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.security.JwtUtil;
import com.yusufziyrek.blogApp.services.abstracts.IAuthService;
import com.yusufziyrek.blogApp.services.concretes.RefreshTokenService;
import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;
import com.yusufziyrek.blogApp.utilites.exceptions.AuthException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

	private final IAuthService authService;
	private final RefreshTokenService refreshTokenService;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
		String response = authService.register(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
		String result = authService.verifyAccount(token);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
		String requestRefreshToken = request.get("refreshToken");
		var optionalToken = refreshTokenService.findByToken(requestRefreshToken);
		if (optionalToken.isEmpty()) {
			throw new AuthException("Refresh token not found!");
		}
		var refreshToken = refreshTokenService.verifyExpiration(optionalToken.get());
		String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getEmail(), refreshToken.getUser().getId());
		return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", refreshToken.getToken()));
	}
}
