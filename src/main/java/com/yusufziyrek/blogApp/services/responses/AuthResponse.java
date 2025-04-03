package com.yusufziyrek.blogApp.services.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
	private String accessToken;
	private String refreshToken;
	private String message;
}
