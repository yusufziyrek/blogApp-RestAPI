package com.yusufziyrek.blogApp.identity.dto.responses;

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
