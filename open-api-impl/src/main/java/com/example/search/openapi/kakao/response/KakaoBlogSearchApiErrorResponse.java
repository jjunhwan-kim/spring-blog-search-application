package com.example.search.openapi.kakao.response;

import lombok.Data;

/**
 * 카카오 블로그 검색 API 오류 응답입니다.
 */
@Data
public class KakaoBlogSearchApiErrorResponse {
	/**
	 * 오류 타입
	 */
	private String errorType;
	/**
	 * 오류 메시지
	 */
	private String message;
}
