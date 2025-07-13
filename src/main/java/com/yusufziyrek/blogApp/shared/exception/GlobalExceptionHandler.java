package com.yusufziyrek.blogApp.shared.exception;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
		return new ResponseEntity<>(detail, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CustomProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.FORBIDDEN.value(), ErrorMessages.ACCESS_DENIED,
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<CustomProblemDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.UNAUTHORIZED.value(), ErrorMessages.INVALID_CREDENTIALS,
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.UNAUTHORIZED);
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

		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.BAD_REQUEST.value(), ErrorMessages.VALIDATION_FAILED,
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		detail.setValidationErrors(errors);

		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CustomProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
		String message = String.format("Invalid parameter type: expected %s for %s", ex.getRequiredType().getSimpleName(), ex.getName());
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.BAD_REQUEST.value(), message,
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<CustomProblemDetail> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.NOT_FOUND.value(), ErrorMessages.RESOURCE_NOT_FOUND,
				request.getDescription(false).substring(4), getMachineName(), LocalDateTime.now());
		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomProblemDetail> handleGenericException(Exception ex, WebRequest request) {
		CustomProblemDetail detail = new CustomProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessages.INTERNAL_SERVER_ERROR,
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
