package com.example.search.openapi.kakao.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.search.openapi.response.BlogSearchApiResponse;
import com.example.search.openapi.response.BlogSearchApiResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 카카오 블로그 검색 API 정상 응답입니다.
 */
@Data
public class KakaoBlogSearchApiResponse {

	/**
	 * meta
	 */
	private Meta meta;
	/**
	 * documents
	 */
	private List<Document> documents;

	/**
	 * KakaoBlogSearchApiResponse 객체를 BlogSearchApiResponse 객체로 변환하여 리턴합니다.
	 * @return BlogSearchApiResponse 객체
	 */
	public BlogSearchApiResponse convert() {
		return new BlogSearchApiResponse(meta.getTotalCount(),
			documents.stream().map(Document::convert)
				.collect(Collectors.toList()));
	}

	/**
	 * 총 검색 결과 개수, 노출 가능 블로그 글 수, 마지막 페이지 여부
	 */
	@Data
	public static class Meta {
		/**
		 * 검색된 블로그 글 수
		 */
		@JsonProperty("total_count")
		private Integer totalCount;
		/**
		 * total_count 중 노출 가능 블로그 글 수
		 */
		@JsonProperty("pageable_count")
		private Integer pageableCount;
		/**
		 * 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
		 */
		@JsonProperty("is_end")
		private Boolean isEnd;
	}

	/**
	 * 검색된 블로그 글
	 */
	@Data
	public static class Document {
		/**
		 * 블로그 글 제목
		 */
		private String title;
		/**
		 * 블로그 글 요약
		 */
		private String contents;
		/**
		 * 블로그 글 URL
		 */
		private String url;
		/**
		 * 블로그 이름
		 */
		@JsonProperty("blogname")
		private String blogName;
		/**
		 * 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
		 */
		private String thumbnail;
		/**
		 * 블로그 글 작성시간, ISO 8601, [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private LocalDateTime datetime;

		/**
		 * Document 객체를 BlogSearchApiResult 객체로 변환하여 리턴합니다.
		 * @return BlogSearchApiResult 객체
		 */
		public BlogSearchApiResult convert() {
			return new BlogSearchApiResult(title, contents, url, blogName, datetime.toLocalDate());
		}
	}
}
