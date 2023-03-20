package com.example.search.openapi.naver.response;

import lombok.Data;

/**
 * 네이버 블로그 검색 API 오류 응답입니다.
 */
@Data
public class NaverBlogSearchApiErrorResponse {
	/**
	 * 오류 코드
	 */
	private String errorCode;
	/**
	 * 오류 메시지
	 */
	private String errorMessage;
}
