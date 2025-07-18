package com.yusufziyrek.blogApp.identity.web.abstracts;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yusufziyrek.blogApp.identity.dto.requests.LoginRequest;
import com.yusufziyrek.blogApp.identity.dto.requests.RegisterRequest;
import com.yusufziyrek.blogApp.identity.dto.responses.AuthResponse;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/auth")
@Validated
public interface IAuthController {

	@PostMapping("/register")
	ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request);

	@PostMapping("/login")
	ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request);

	@GetMapping("/verify")
	ResponseEntity<ApiResponse<String>> verifyAccount(@RequestParam("token") String token);

	@PostMapping("/refresh")
	ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(@RequestBody @Valid Map<String, String> request);
}
