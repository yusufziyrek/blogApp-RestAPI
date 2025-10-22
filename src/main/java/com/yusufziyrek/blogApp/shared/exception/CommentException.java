package com.yusufziyrek.blogApp.shared.exception;

import org.springframework.http.HttpStatus;

public class CommentException extends RuntimeException {

	private final HttpStatus status;

	public CommentException(String message) {
		this(message, HttpStatus.BAD_REQUEST);
	}

	public CommentException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
