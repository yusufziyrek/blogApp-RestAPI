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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Domain'e özel istisnalar
    @ExceptionHandler(UserException.class)
    public ResponseEntity<CustomProblemDetail> handleUserException(UserException ex, WebRequest request) {
        log.warn("User exception: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        CustomProblemDetail detail = buildProblemDetail(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(detail);
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<CustomProblemDetail> handlePostException(PostException ex, WebRequest request) {
        log.warn("Post exception: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        CustomProblemDetail detail = buildProblemDetail(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(detail);
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<CustomProblemDetail> handleCommentException(CommentException ex, WebRequest request) {
        log.warn("Comment exception: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        CustomProblemDetail detail = buildProblemDetail(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(detail);
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<CustomProblemDetail> handleLikeException(LikeException ex, WebRequest request) {
        log.warn("Like exception: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        CustomProblemDetail detail = buildProblemDetail(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(detail);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomProblemDetail> handleAuthException(AuthException ex, WebRequest request) {
        log.warn("Auth exception: {}", ex.getMessage());
        HttpStatus status = ex.getStatus();
        CustomProblemDetail detail = buildProblemDetail(status, ex.getMessage(), request);
        return ResponseEntity.status(status).body(detail);
    }

    // Validation errors için temiz mesajlar
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomProblemDetail> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        CustomProblemDetail detail = buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorMessages.VALIDATION_FAILED,
                request);
        detail.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(detail);
    }

    // Kimlik doğrulama hataları
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomProblemDetail> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.UNAUTHORIZED, ErrorMessages.AUTHENTICATION_REQUIRED,
                request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detail);
    }

    // Yetkilendirme hataları
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomProblemDetail> handleAccessDeniedException(AccessDeniedException ex,
            WebRequest request) {
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.FORBIDDEN, ErrorMessages.ACCESS_DENIED, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(detail);
    }

    // Geçersiz istek hataları
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ARGUMENT,
                request);
        return ResponseEntity.badRequest().body(detail);
    }

    // Tip uyumsuzluğu hataları
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomProblemDetail> handleTypeMismatchException(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String message = "me".equals(ex.getValue())
                ? ErrorMessages.INVALID_ENDPOINT_USAGE
                : ErrorMessages.INVALID_PARAMETER_FORMAT;

        CustomProblemDetail detail = buildProblemDetail(HttpStatus.BAD_REQUEST, message, request);
        return ResponseEntity.badRequest().body(detail);
    }

    // JSON ayrıştırma hataları
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            WebRequest request) {
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_PAYLOAD_FORMAT,
                request);
        return ResponseEntity.badRequest().body(detail);
    }

    // Null pointer hataları
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomProblemDetail> handleNullPointerException(NullPointerException ex, WebRequest request) {
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorMessages.REQUIRED_FIELDS_MISSING,
                request);
        return ResponseEntity.badRequest().body(detail);
    }

    // Genel exception handler (bilinmeyen hatalar için)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomProblemDetail> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        CustomProblemDetail detail = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessages.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(detail);
    }

    private String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

    private CustomProblemDetail buildProblemDetail(HttpStatus status, String message, WebRequest request) {
        return new CustomProblemDetail(
                status.value(),
                message,
                request.getDescription(false).replace("uri=", ""),
                getMachineName(),
                LocalDateTime.now());
    }
}
