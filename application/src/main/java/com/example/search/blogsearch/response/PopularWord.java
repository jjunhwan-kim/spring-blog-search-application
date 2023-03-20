package com.example.search.blogsearch.response;

import com.example.search.blogsearch.entity.BlogSearch;

import lombok.Getter;

/**
 * 인기 검색어
 */
@Getter
public class PopularWord {
	/**
	 * 검색 키워드
	 */
	private final String word;
	/**
	 * 검색 횟수
	 */
	private final Long count;

	/**
	 * 인기 검색어를 생성합니다.
	 * @param word 검색 키워드
	 * @param count 검색 횟수
	 */
	private PopularWord(String word, Long count) {
		this.word = word;
		this.count = count;
	}

	/**
	 * 인기 검색어를 생성합니다.
	 * @param blogSearch BlogSearch 객체
	 * @return PopularWord 객체
	 */
	public static PopularWord of(BlogSearch blogSearch) {
		return new PopularWord(blogSearch.getWord(), blogSearch.getCount());
	}
}
