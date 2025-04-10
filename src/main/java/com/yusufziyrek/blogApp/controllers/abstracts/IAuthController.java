package com.yusufziyrek.blogApp.controllers.abstracts;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;

import jakarta.validation.Valid;

import java.util.Map;

@RequestMapping("/api/v1/auth")
@Validated
public interface IAuthController {

	@PostMapping("/register")
	ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request);

	@PostMapping("/login")
	ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request);

	@GetMapping("/verify")
	ResponseEntity<String> verifyAccount(@RequestParam("token") String token);

	@PostMapping("/refresh")
	ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request);
}
