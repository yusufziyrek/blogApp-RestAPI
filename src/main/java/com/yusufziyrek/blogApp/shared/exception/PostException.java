package com.yusufziyrek.blogApp.shared.exception;

import org.springframework.http.HttpStatus;

public class PostException extends RuntimeException {

	private final HttpStatus status;

	public PostException(String message) {
		this(message, HttpStatus.BAD_REQUEST);
	}

	public PostException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
