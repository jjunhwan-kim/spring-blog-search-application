package com.example.search.openapi.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 블로그 검색 API 파라미터 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class BlogSearchApiRequest {
	/**
	 * 검색 키워드
	 */
	private final String word;

	/**
	 * 정렬 타입
	 */
	private final BlogSearchApiSortType sortType;

	/**
	 * 페이지 번호
	 */
	private final Integer page;

	/**
	 * 한 페이지에 보여질 블로그 글 수
	 */
	private final Integer size;
}
