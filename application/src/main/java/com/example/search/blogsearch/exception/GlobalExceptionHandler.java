package com.example.search.blogsearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.search.openapi.exception.BlogSearchApiClientErrorException;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;

/**
 * 전역적으로 예외를 처리합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * BlogSearchApiClientErrorException 예외를 처리합니다.
	 * @param exception BlogSearchApiClientErrorException 예외
	 * @return 400 오류를 리턴합니다.
	 */
	@ExceptionHandler(BlogSearchApiClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleBlogSearchApiClientErrorException(
		BlogSearchApiClientErrorException exception) {

		ErrorResponse errorResponse = new ErrorResponse(exception.getCode(), exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	/**
	 * BlogSearchApiServerErrorException 예외를 처리합니다.
	 * @param exception BlogSearchApiServerErrorException 예외
	 * @return 500 오류를 리턴합니다.
	 */
	@ExceptionHandler(BlogSearchApiServerErrorException.class)
	public ResponseEntity<ErrorResponse> handleBlogSearchApiServerErrorException(
		BlogSearchApiServerErrorException exception) {

		ErrorResponse errorResponse = new ErrorResponse(exception.getCode(), exception.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}

	/**
	 * 그 외의 모든 예외를 처리합니다.
	 * @param exception BlogSearchApiClientErrorException, BlogSearchApiServerErrorException 예외를 제외한 모든 예외
	 * @return 500 오류를 리턴합니다.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {

		ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "Internal Server Error");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(errorResponse);
	}
}
