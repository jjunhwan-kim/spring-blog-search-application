package com.example.search.blogsearch.request;

import lombok.Data;

/**
 * 블로그 검색 요청
 */
@Data
public class BlogSearchRequest {
	/**
	 * 검색 키워드
	 */
	private String word;
	/**
	 * 정렬
	 */
	private String sort;
	/**
	 * 페이지 번호
	 */
	private Integer page;
	/**
	 * 한 페이지에 보여질 블로그 글 수
	 */
	private Integer size;

	/**
	 * 블로그 검색 요청을 생성합니다.
	 * @param word 검색 키워드
	 * @param sort 정렬
	 * @param page 페이지 번호
	 * @param size 한 페이지에 보여질 블로그 글 수
	 */
	public BlogSearchRequest(String word, String sort, Integer page, Integer size) {
		this.word = word;
		this.sort = sort;
		this.page = page;
		this.size = size;
	}
}
