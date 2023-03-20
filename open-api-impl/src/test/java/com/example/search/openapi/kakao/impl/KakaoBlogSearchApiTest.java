package com.example.search.openapi.kakao.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.request.BlogSearchApiSortType;
import com.example.search.openapi.response.BlogSearchApiResponse;

@SpringBootTest
class KakaoBlogSearchApiTest {

	@Autowired
	KakaoBlogSearchApi kakaoBlogSearchApi;

	@Test
	@DisplayName("카카오 블로그 검색 API 테스트")
	void kakaoBlogSearchApiTest1() {

		// Given
		BlogSearchApiRequest request = new BlogSearchApiRequest("test",
			BlogSearchApiSortType.ACCURACY,
			null, null);

		BlogSearchApiResponse response = kakaoBlogSearchApi.search(request);

		Assertions.assertThat(response.getTotal()).isGreaterThan(0);
		Assertions.assertThat(response.getResults().size()).isGreaterThan(0);
	}

}