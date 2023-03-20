package com.example.search.openapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 블로그 검색 API 예외를 위한 추상클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public abstract class BlogSearchApiException extends RuntimeException {
	/**
	 * 오류 코드
	 */
	private final String code;

	/**
	 * 오류 메시지
	 */
	private final String message;
}
