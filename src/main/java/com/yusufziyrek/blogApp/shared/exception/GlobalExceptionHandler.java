package com.yusufziyrek.blogApp.shared.exception;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<CustomProblemDetail> handleUserException(UserException ex, WebRequest request) {
		log.warn("UserException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PostException.class)
	public ResponseEntity<CustomProblemDetail> handlePostException(PostException ex, WebRequest request) {
		log.warn("PostException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CommentException.class)
	public ResponseEntity<CustomProblemDetail> handleCommentException(CommentException ex, WebRequest request) {
		log.warn("CommentException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LikeException.class)
	public ResponseEntity<CustomProblemDetail> handleLikeException(LikeException ex, WebRequest request) {
		log.warn("LikeException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<CustomProblemDetail> handleAuthException(AuthException ex, WebRequest request) {
		log.warn("AuthException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CustomProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		log.warn("AccessDeniedException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.FORBIDDEN.value(), "Access denied",
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<CustomProblemDetail> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		log.warn("BadCredentialsException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials",
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<CustomProblemDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		log.warn("AuthenticationException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.UNAUTHORIZED.value(), "Authentication failed",
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
			WebRequest request) {
		log.warn("ValidationException: {}", ex.getMessage());
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

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CustomProblemDetail> handleTypeMismatchException(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		log.warn("TypeMismatchException: {}", ex.getMessage());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.BAD_REQUEST.value(), 
				"Invalid parameter type: " + ex.getName(),
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomProblemDetail> handleGenericException(Exception ex, WebRequest request) {
		log.error("Unexpected error: {}", ex.getMessage(), ex);
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
				"An unexpected error occurred",
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String getMachineName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "Unknown";
		}
	}
}
