package com.example.search.blogsearch.response;

import java.time.LocalDate;

import com.example.search.openapi.response.BlogSearchApiResult;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

/**
 * 검색된 블로그 글
 */
@Getter
public class BlogSearchResult {
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
	 * 블로그 글 작성일
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private final LocalDate date;

	/**
	 * 검색된 블로그 글을 생성합니다.
	 * @param title 글 제목
	 * @param contents 글 요약
	 * @param url URL
	 * @param blogName 블로그 이름
	 * @param date 작성일
	 */
	private BlogSearchResult(String title, String contents, String url, String blogName, LocalDate date) {
		this.title = title;
		this.contents = contents;
		this.url = url;
		this.blogName = blogName;
		this.date = date;
	}

	/**
	 * 검색된 블로그 글을 생성합니다.
	 * @param result BlogSearchApiResult 객체
	 * @return BlogSearchResult 객체
	 */
	public static BlogSearchResult of(BlogSearchApiResult result) {
		return new BlogSearchResult(result.getTitle(), result.getContents(), result.getUrl(), result.getBlogName(),
			result.getDate());
	}
}
