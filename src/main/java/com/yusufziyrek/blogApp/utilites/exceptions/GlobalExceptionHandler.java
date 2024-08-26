package com.yusufziyrek.blogApp.utilites.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ProblemDetail brandAlreadyExistsException(UserException e) {

		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

		return detail;

	}

	@ExceptionHandler(PostException.class)
	public ProblemDetail modelAlreadyExistsException(PostException e) {

		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

		return detail;

	}

	@ExceptionHandler(CommentException.class)
	public ProblemDetail carAlreadyExistsException(CommentException e) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

		return detail;
	}

	@ExceptionHandler(LikeException.class)
	public ProblemDetail likeException(LikeException e) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

		return detail;

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail validException(MethodArgumentNotValidException e) {
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Valid exception !");

		return detail;

	}

}
