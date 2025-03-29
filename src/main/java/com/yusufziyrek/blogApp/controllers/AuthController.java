package com.yusufziyrek.blogApp.controllers;

import com.yusufziyrek.blogApp.services.requests.LoginRequest;
import com.yusufziyrek.blogApp.services.requests.RegisterRequest;
import com.yusufziyrek.blogApp.services.responses.AuthResponse;
import com.yusufziyrek.blogApp.services.concretes.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
		String response = authService.register(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
		String result = authService.verifyAccount(token);
		return ResponseEntity.ok(result);
	}
}
