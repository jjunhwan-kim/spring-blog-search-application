package com.example.search.blogsearch.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 오류 응답입니다.
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
	/**
	 * 오류 코드
	 */
	private final String code;
	/**
	 * 오류 메시지
	 */
	private final String message;
}
