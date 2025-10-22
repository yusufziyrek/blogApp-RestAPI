package com.yusufziyrek.blogApp.shared.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

	private final HttpStatus status;

	public AuthException(String message) {
		this(message, HttpStatus.UNAUTHORIZED);
	}

	public AuthException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
