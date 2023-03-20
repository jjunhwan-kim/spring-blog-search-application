package com.example.search.blogsearch.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 인기 검색어 목록
 */
@Getter
@RequiredArgsConstructor
public class PopularWordsResponse {
	/**
	 * 인기 검색어 목록
	 */
	private final List<PopularWord> words;
}
