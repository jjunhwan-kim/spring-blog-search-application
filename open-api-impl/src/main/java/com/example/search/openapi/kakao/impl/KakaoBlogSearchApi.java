package com.example.search.openapi.kakao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.search.openapi.BlogSearchApi;
import com.example.search.openapi.exception.BlogSearchApiClientErrorException;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;
import com.example.search.openapi.kakao.response.KakaoBlogSearchApiErrorResponse;
import com.example.search.openapi.kakao.response.KakaoBlogSearchApiResponse;
import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.request.BlogSearchApiSortType;
import com.example.search.openapi.response.BlogSearchApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 블로그 검색 인터페이스 카카오 구현체입니다.
 * 빈 우선순위를 0으로 설정합니다.
 */
@Order(0)
@Component
public class KakaoBlogSearchApi implements BlogSearchApi {

	/**
	 * 카카오 블로그 검색 API URL 입니다.
	 */
	private static final String BLOG_SEARCH_URL = "https://dapi.kakao.com/v2/search/blog";
	/**
	 * kakaoRestTemplate 빈을 주입받습니다.
	 */
	private final RestTemplate restTemplate;
	/**
	 * JSON 문자열을 자바 객체로 역 직렬화하기 위한 ObjectMapper 빈을 주입받습니다.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * 블로그 검색 인터페이스 카카오 구현체를 생성합니다.
	 * @param restTemplate kakaoRestTemplate 스프링 빈
	 * @param objectMapper ObjectMapper 스프링 빈
	 */
	public KakaoBlogSearchApi(@Qualifier("kakaoRestTemplate") RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * 카카오 블로그 검색 API를 호출하여 블로그 글을 검색하여 리턴합니다.
	 * @throws BlogSearchApiClientErrorException
	 *          카카오 블로그 검색 API 응답이 400 오류인 경우
	 * @throws BlogSearchApiServerErrorException
	 *          나머지 모든 예외상황
	 * @param request 블로그 검색 키워드, 정렬 타입, 페이지 번호, 한 페이지에 보여질 블로그 글 수
	 * @return 총 검색 결과 개수, 검색된 블로그 글 목록
	 */
	@Override
	public BlogSearchApiResponse search(BlogSearchApiRequest request) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BLOG_SEARCH_URL)
			.queryParam("query", request.getWord());

		if (request.getSortType().equals(BlogSearchApiSortType.ACCURACY)) {
			builder.queryParam("sort", "accuracy");
		} else if (request.getSortType().equals(BlogSearchApiSortType.RECENCY)) {
			builder.queryParam("sort", "recency");
		}

		if (request.getPage() != null) {
			builder.queryParam("page", request.getPage());
		}
		if (request.getSize() != null) {
			builder.queryParam("size", request.getSize());
		}

		String url = builder.build().toString();

		try {
			KakaoBlogSearchApiResponse response = restTemplate.getForObject(url, KakaoBlogSearchApiResponse.class,
				request.getWord());

			if (response == null) {
				throw new BlogSearchApiServerErrorException();
			}

			return response.convert();

		} catch (HttpClientErrorException exception) {
			try {
				KakaoBlogSearchApiErrorResponse errorResponse = objectMapper.readValue(
					exception.getResponseBodyAsString(), KakaoBlogSearchApiErrorResponse.class);
				throw new BlogSearchApiClientErrorException(errorResponse.getErrorType(), errorResponse.getMessage());
			} catch (JsonProcessingException e) {
				throw new BlogSearchApiServerErrorException();
			}
		} catch (Exception exception) {
			throw new BlogSearchApiServerErrorException();
		}
	}
}
