package com.example.search.openapi.naver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 네이버 블로그 검색 API를 호출하기 위한 RestTemplate 스프링 빈 설정입니다.
 */
@Configuration
public class NaverRestTemplateConfig {

	/**
	 * 클라이언트 아이디 값을 담을 헤더 이름입니다.
	 */
	private static final String CLIENT_ID_HEADER_NAME = "X-Naver-Client-Id";
	/**
	 * 클라이언트 시크릿 값을 담을 헤더 이름입니다.
	 */
	private static final String CLIENT_SECRET_HEADER_NAME = "X-Naver-Client-Secret";
	/**
	 * 클라이언트 아이디 값 입니다.
	 */
	@Value("${openapi.naver.client-id}")
	private String clientId;
	/**
	 * 클라이언트 시크릿 값 입니다.
	 */
	@Value("${openapi.naver.client-secret}")
	private String clientSecret;

	/**
	 * 네이버 블로그 검색 API를 호출하기 위한 RestTemplate 스프링 빈을 생성합니다.
	 * @param restTemplateBuilder RestTemplateBuilder
	 * @return RestTemplate
	 */
	@Qualifier("naverRestTemplate")
	@Bean
	public RestTemplate naverRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.defaultHeader(CLIENT_ID_HEADER_NAME, clientId)
			.defaultHeader(CLIENT_SECRET_HEADER_NAME, clientSecret)
			.build();
	}
}
