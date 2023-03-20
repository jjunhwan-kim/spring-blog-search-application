package com.example.search.openapi.exception;

import lombok.Getter;

/**
 *  블로그 검색 API 요청시 응답이 서버 오류 일 경우 던집니다.
 */
@Getter
public class BlogSearchApiServerErrorException extends BlogSearchApiException {
	public BlogSearchApiServerErrorException() {
		super("Internal Server Error", "Internal Server Error");
	}
}
