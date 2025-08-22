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

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Domain'e özel istisnalar
    @ExceptionHandler(UserException.class)
    public ResponseEntity<CustomProblemDetail> handleUserException(UserException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<CustomProblemDetail> handlePostException(PostException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.NOT_FOUND.value(), 
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<CustomProblemDetail> handleCommentException(CommentException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.NOT_FOUND.value(), 
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    @ExceptionHandler(LikeException.class)
    public ResponseEntity<CustomProblemDetail> handleLikeException(LikeException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.NOT_FOUND.value(), 
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomProblemDetail> handleAuthException(AuthException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.UNAUTHORIZED.value(), 
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detail);
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

        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                "Girilen bilgiler geçersiz",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        detail.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(detail);
    }

    // Kimlik doğrulama hataları
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomProblemDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.UNAUTHORIZED.value(), 
                "Giriş yapmanız gerekiyor",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detail);
    }

    // Yetkilendirme hataları
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.FORBIDDEN.value(), 
                "Bu işlem için yetkiniz bulunmuyor",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(detail);
    }

    // Geçersiz istek hataları
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                "Geçersiz parametre",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(detail);
    }

    // Tip uyumsuzluğu hataları
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomProblemDetail> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message;
        if ("me".equals(ex.getValue())) {
            message = "Geçersiz endpoint kullanımı";
        } else {
            message = "Geçersiz parametre formatı";
        }
        
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                message,
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(detail);
    }

    // JSON ayrıştırma hataları
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                "Gönderilen veri formatı hatalı",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(detail);
    }

    // Null pointer hataları
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomProblemDetail> handleNullPointerException(NullPointerException ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.BAD_REQUEST.value(), 
                "Gerekli alanlar eksik",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(detail);
    }

    // Genel exception handler (bilinmeyen hatalar için)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomProblemDetail> handleGeneralException(Exception ex, WebRequest request) {
        CustomProblemDetail detail = new CustomProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Sistem hatası oluştu. Lütfen daha sonra tekrar deneyin",
                request.getDescription(false).replace("uri=", ""), 
                getMachineName(), 
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(detail);
    }

    private String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }
}
