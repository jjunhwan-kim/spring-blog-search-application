package com.example.search.blogsearch.controller;

import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.search.blogsearch.entity.BlogSearch;
import com.example.search.blogsearch.response.BlogSearchResponse;
import com.example.search.blogsearch.response.BlogSearchResult;
import com.example.search.blogsearch.response.PopularWord;
import com.example.search.blogsearch.response.PopularWordsResponse;
import com.example.search.blogsearch.service.BlogSearchApiService;
import com.example.search.openapi.exception.BlogSearchApiClientErrorException;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;
import com.example.search.openapi.response.BlogSearchApiResult;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BlogSearchApiService blogSearchApiService;

	@Test
	@DisplayName("블로그 검색")
	void blogSearchControllerTest1() throws Exception {
		// Given
		given(blogSearchApiService.search(any()))
			.willReturn(new BlogSearchResponse(5,
				List.of(BlogSearchResult.of(
						new BlogSearchApiResult(
							"title1",
							"content1",
							"url1",
							"blog1",
							LocalDate.of(2023, 1, 1))),
					BlogSearchResult.of(
						new BlogSearchApiResult(
							"title2",
							"content2",
							"url2",
							"blog2",
							LocalDate.of(2023, 1, 2))))));

		// When & Then
		mockMvc.perform(get("/search")
				.param("word", "test")
				.param("sort", "accuracy")
				.param("page", "1")
				.param("size", "10"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.total").value(5))
			.andExpect(jsonPath("$.results[*].title").value(contains("title1", "title2")))
			.andExpect(jsonPath("$.results[*].contents").value(contains("content1", "content2")))
			.andExpect(jsonPath("$.results[*].url").value(contains("url1", "url2")))
			.andExpect(jsonPath("$.results[*].blogName").value(contains("blog1", "blog2")))
			.andExpect(jsonPath("$.results[*].date").value(contains("20230101", "20230102")));
	}

	@Test
	@DisplayName("블로그 검색 클라이언트 오류 400 코드 리턴")
	void blogSearchControllerTest2() throws Exception {
		// Given
		given(blogSearchApiService.search(any())).willThrow(new BlogSearchApiClientErrorException("code", "message"));

		// When & Then
		mockMvc.perform(get("/search")
				.param("word", "test")
				.param("sort", "accuracy")
				.param("page", "1")
				.param("size", "10"))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.code").value("code"))
			.andExpect(jsonPath("$.message").value("message"));
	}

	@Test
	@DisplayName("블로그 검색 서버 오류 500 코드 리턴")
	void blogSearchControllerTest3() throws Exception {
		// Given
		given(blogSearchApiService.search(any())).willThrow(new BlogSearchApiServerErrorException());

		// When & Then
		mockMvc.perform(get("/search")
				.param("word", "test")
				.param("sort", "accuracy")
				.param("page", "1")
				.param("size", "10"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code").value("Internal Server Error"))
			.andExpect(jsonPath("$.message").value("Internal Server Error"));
	}

	@Test
	@DisplayName("블로그 검색 알 수 없는 오류 코드 500 리턴")
	void blogSearchControllerTest4() throws Exception {
		// Given
		given(blogSearchApiService.search(any())).willThrow(new RuntimeException());

		// When & Then
		mockMvc.perform(get("/search")
				.param("word", "test")
				.param("sort", "accuracy")
				.param("page", "1")
				.param("size", "10"))
			.andDo(print())
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.code").value("Internal Server Error"))
			.andExpect(jsonPath("$.message").value("Internal Server Error"));
	}

	@Test
	@DisplayName("인기 검색어 목록 조회")
	void blogSearchControllerTest5() throws Exception {
		// Given
		given(blogSearchApiService.getPopularWords())
			.willReturn(new PopularWordsResponse(
				List.of(PopularWord.of(new BlogSearch("word1", 2L)),
					PopularWord.of(new BlogSearch("word2", 1L)))));

		// When & Then
		mockMvc.perform(get("/popular-words"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.words[0].word").value("word1"))
			.andExpect(jsonPath("$.words[0].count").value(2))
			.andExpect(jsonPath("$.words[1].word").value("word2"))
			.andExpect(jsonPath("$.words[1].count").value(1));
	}
}