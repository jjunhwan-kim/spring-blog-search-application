package com.example.search.openapi.kakao.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 카카오 블로그 검색 API를 호출하기 위한 RestTemplate 스프링 빈 설정입니다.
 */
@Configuration
public class KakaoRestTemplateConfig {

	/**
	 * REST API Key를 담을 헤더 이름입니다.
	 */
	private static final String CLIENT_ID_HEADER_NAME = "Authorization";
	/**
	 * Authorization 헤더에 담을 REST API Key Prefix 입니다.
	 */
	private static final String CLIENT_ID_PREFIX = "KakaoAK ";
	/**
	 * Authorization 헤더에 담을 REST API Key 입니다.
	 */
	@Value("${openapi.kakao.client-id}")
	private String clientId;

	/**
	 * 카카오 블로그 검색 API를 호출하기 위한 RestTemplate 스프링 빈을 생성합니다.
	 * @param restTemplateBuilder RestTemplateBuilder
	 * @return RestTemplate
	 */
	@Qualifier("kakaoRestTemplate")
	@Bean
	public RestTemplate kakaoRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.defaultHeader(CLIENT_ID_HEADER_NAME, CLIENT_ID_PREFIX + clientId)
			.build();
	}
}
