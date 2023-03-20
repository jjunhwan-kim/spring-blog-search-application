package com.example.search.openapi.naver.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.search.openapi.response.BlogSearchApiResponse;
import com.example.search.openapi.response.BlogSearchApiResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 네이버 블로그 검색 API 정상 응답입니다.
 */
@Data
public class NaverBlogSearchApiResponse {

	/**
	 * 검색 결과를 생성한 시간
	 */
	private String lastBuildDate;

	/**
	 * 총 검색 결과 개수
	 */
	private Integer total;
	/**
	 * 검색 시작 위치
	 */
	private Integer start;
	/**
	 * 한 번에 표시할 검색 결과 개수
	 */
	private Integer display;
	/**
	 * 개별 검색 결과. JSON 형식의 결괏값에서는 items 속성의 JSON 배열로 개별 검색 결과를 반환합니다.
	 */
	@JsonProperty("items")
	private List<Document> documents;

	@Data
	public static class Document {
		/**
		 * 블로그 포스트의 제목
		 */
		private String title;
		/**
		 * 블로그 포스트의 URL
		 */
		private String link;
		/**
		 * 블로그 포스트의 내용을 요약한 패시지 정보
		 */
		private String description;
		/**
		 * 블로그 포스트가 있는 블로그의 이름
		 */
		@JsonProperty("bloggername")
		private String blogName;
		/**
		 * 블로그 포스트가 있는 블로그의 주소
		 */
		@JsonProperty("bloggerlink")
		private String blogLink;
		/**
		 * 블로그 포스트가 작성된 날짜
		 */
		@JsonProperty("postdate")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
		private LocalDate date;

		/**
		 * Document 객체를 BlogSearchApiResult 객체로 변환하여 리턴합니다.
		 * @return BlogSearchApiResult 객체
		 */
		public BlogSearchApiResult convert() {
			return new BlogSearchApiResult(title, description, link, blogName, date);
		}
	}

	/**
	 * NaverBlogSearchApiResponse 객체를 BlogSearchApiResponse 객체로 변환하여 리턴합니다.
	 * @return BlogSearchApiResponse 객체
	 */
	public BlogSearchApiResponse convert() {
		return new BlogSearchApiResponse(total,
			documents.stream().map(Document::convert)
				.collect(Collectors.toList()));
	}
}
