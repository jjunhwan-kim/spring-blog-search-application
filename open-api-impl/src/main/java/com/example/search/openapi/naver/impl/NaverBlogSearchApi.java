package com.example.search.openapi.naver.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.search.openapi.BlogSearchApi;
import com.example.search.openapi.exception.BlogSearchApiClientErrorException;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;
import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.request.BlogSearchApiSortType;
import com.example.search.openapi.response.BlogSearchApiResponse;
import com.example.search.openapi.naver.response.NaverBlogSearchApiErrorResponse;
import com.example.search.openapi.naver.response.NaverBlogSearchApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 블로그 검색 인터페이스 네이버 구현체입니다.
 * 빈 우선순위를 1으로 설정합니다.
 */
@Order(1)
@Component
public class NaverBlogSearchApi implements BlogSearchApi {

	/**
	 * 네이버 블로그 검색 API URL 입니다.
	 */
	private static final String BLOG_SEARCH_URL = "https://openapi.naver.com/v1/search/blog.json";
	/**
	 * naverRestTemplate 빈을 주입받습니다.
	 */
	private final RestTemplate restTemplate;
	/**
	 * JSON 문자열을 자바 객체로 역 직렬화하기 위한 ObjectMapper 빈을 주입받습니다.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * 블로그 검색 인터페이스 네이버 구현체를 생성합니다.
	 * @param restTemplate naverRestTemplate 스프링 빈
	 * @param objectMapper ObjectMapper 스프링 빈
	 */
	public NaverBlogSearchApi(@Qualifier("naverRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * 네이버 블로그 검색 API를 호출하여 블로그 글을 검색하여 리턴합니다.
	 * @throws  BlogSearchApiClientErrorException
	 *          네이버 블로그 검색 API 응답이 400 오류인 경우
	 * @throws  BlogSearchApiServerErrorException
	 *          나머지 모든 예외상황
	 * @param request 블로그 검색 키워드, 정렬 타입, 페이지 번호, 한 페이지에 보여질 문서 수
	 * @return 총 검색 결과 개수, 검색된 문서 목록
	 */
	@Override
	public BlogSearchApiResponse search(BlogSearchApiRequest request) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BLOG_SEARCH_URL)
			.queryParam("query", request.getWord());

		if (request.getSortType().equals(BlogSearchApiSortType.ACCURACY)) {
			builder.queryParam("sort", "sim");
		} else if (request.getSortType().equals(BlogSearchApiSortType.RECENCY)) {
			builder.queryParam("sort", "date");
		}

		if (request.getPage() != null) {
			// start는 검색 시작 위치이므로, page 값을 start 값으로 변환해야함
			int start = (request.getPage() - 1) * request.getSize() + 1;
			builder.queryParam("start", start);
		}
		if (request.getSize() != null) {
			builder.queryParam("display", request.getSize());
		}

		String url = builder.build().toString();

		try {
			NaverBlogSearchApiResponse response = restTemplate.getForObject(url, NaverBlogSearchApiResponse.class,
				request.getWord());

			if (response == null) {
				throw new BlogSearchApiServerErrorException();
			}

			return response.convert();

		} catch (HttpClientErrorException exception) {
			try {
				NaverBlogSearchApiErrorResponse errorResponse = objectMapper.readValue(
					exception.getResponseBodyAsString(), NaverBlogSearchApiErrorResponse.class);
				throw new BlogSearchApiClientErrorException(errorResponse.getErrorCode(),
					errorResponse.getErrorMessage());
			} catch (JsonProcessingException e) {
				throw new BlogSearchApiServerErrorException();
			}
		} catch (Exception exception) {
			throw new BlogSearchApiServerErrorException();
		}
	}
}
