package com.example.search.openapi.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 블로그 검색 API 응답 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class BlogSearchApiResponse {
	/**
	 * 총 검색 결과 개수
	 */
	private final Integer total;
	/**
	 * 검색된 블로그 글 목록
	 */
	private final List<BlogSearchApiResult> results;
}
