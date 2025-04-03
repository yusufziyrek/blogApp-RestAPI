package com.yusufziyrek.blogApp.utilites.exceptions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<CustomProblemDetail> handleUserException(UserException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<CustomProblemDetail> handlePostException(PostException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CommentException.class)
	public ResponseEntity<CustomProblemDetail> handleCommentException(CommentException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LikeException.class)
	public ResponseEntity<CustomProblemDetail> handleLikeException(LikeException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<CustomProblemDetail> handleAuthException(AuthException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
			WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.BAD_REQUEST.value(), "Validation failed",
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		detail.setValidationErrors(errors);

		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}

	private String getMachineName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "Unknown";
		}
	}

}
