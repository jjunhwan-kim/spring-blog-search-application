package com.example.search.blogsearch.service;

import com.example.search.blogsearch.response.PopularWordsResponse;

/**
 * DB에 블로그 검색 키워드, 횟수를 저장하고 인기 검색어 목록을 조회합니다.
 */
public interface BlogSearchService {
	/**
	 * 검색 키워드의 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	void updateWordCount(String word);

	/**
	 * 검색 키워드가 DB에 없다면 삽입하고, 있다면 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	void saveOrUpdateWordCount(String word);

	/**
	 * 인기 검색어 목록을 조회합니다.
	 * @return 인기 검색어 목록, 검색 키워드와 검색 횟수
	 */
	PopularWordsResponse getPopularWords();
}
