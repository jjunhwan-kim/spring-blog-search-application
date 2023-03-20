package com.example.search.openapi.exception;

import lombok.Getter;

/**
 * 블로그 검색 API 요청시 응답이 클라이언트 오류 일 경우 던집니다.
 */
@Getter
public class BlogSearchApiClientErrorException extends BlogSearchApiException {
	public BlogSearchApiClientErrorException(String code, String message) {
		super(code, message);
	}
}
