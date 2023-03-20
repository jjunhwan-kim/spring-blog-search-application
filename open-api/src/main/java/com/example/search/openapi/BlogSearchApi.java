package com.example.search.openapi;

import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.response.BlogSearchApiResponse;

/**
 * 블로그 검색을 나타내는 인터페이스입니다.
 */
public interface BlogSearchApi {
	/**
	 * 블로그 글을 검색하여 리턴합니다.
	 * @param request 블로그 검색 키워드, 정렬 타입, 페이지 번호, 한 페이지에 보여질 블로그 글 수
	 * @return 총 검색 결과 개수, 검색된 블로그 글 목록
	 */
	BlogSearchApiResponse search(BlogSearchApiRequest request);
}
