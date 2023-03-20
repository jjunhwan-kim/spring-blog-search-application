package com.example.search.blogsearch.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 블로그 검색 응답
 */
@Getter
@RequiredArgsConstructor
public class BlogSearchResponse {
	/**
	 * 총 검색 결과 개수
	 */
	private final Integer total;
	/**
	 * 검색된 블로그 글 목록
	 */
	private final List<BlogSearchResult> results;
}
