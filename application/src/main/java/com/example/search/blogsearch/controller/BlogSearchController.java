package com.example.search.blogsearch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.search.blogsearch.request.BlogSearchRequest;
import com.example.search.blogsearch.response.BlogSearchResponse;
import com.example.search.blogsearch.response.PopularWordsResponse;
import com.example.search.blogsearch.service.BlogSearchApiService;

import lombok.RequiredArgsConstructor;

/**
 * 사용자의 블로그 검색 요청을 처리합니다.
 */
@RequiredArgsConstructor
@RestController
public class BlogSearchController {

	/**
	 * 블로그 검색 서비스
	 */
	private final BlogSearchApiService blogSearchApiService;

	/**
	 * 블로그 검색 요청을 처리하고 결과를 리턴합니다.
	 * @param word 검색 키워드
	 * @param sort 정렬
	 * @param page 페이지 번호
	 * @param size 한 페이지에 보여질 블로그 글 수
	 * @return 블로그 검색 결과, 총 검색 결과 개수, 검색된 블로그 글 목록
	 */
	@GetMapping("/search")
	public BlogSearchResponse search(
		@RequestParam String word,
		@RequestParam(required = false, defaultValue = "accuracy") String sort,
		@RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "10") Integer size) {

		BlogSearchRequest request = new BlogSearchRequest(word, sort, page, size);

		return blogSearchApiService.search(request);
	}

	/**
	 * 인기 검색어 목록을 리턴합니다.
	 * @return 인기 검색어 목록, 검색 키워드, 검색 횟수
	 */
	@GetMapping("/popular-words")
	public PopularWordsResponse getPopularWords() {
		return blogSearchApiService.getPopularWords();
	}
}
