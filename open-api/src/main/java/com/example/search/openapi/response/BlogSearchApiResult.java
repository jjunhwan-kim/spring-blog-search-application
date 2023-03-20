package com.example.search.openapi.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 검색된 하나의 블로그 글 입니다.
 */
@Getter
@RequiredArgsConstructor
public class BlogSearchApiResult {
	/**
	 * 블로그 글 제목
	 */
	private final String title;
	/**
	 * 블로그 글 요약
	 */
	private final String contents;
	/**
	 * 블로그 글 URL
	 */
	private final String url;
	/**
	 * 블로그 이름
	 */
	private final String blogName;
	/**
	 * 작성일
	 */
	private final LocalDate date;
}
