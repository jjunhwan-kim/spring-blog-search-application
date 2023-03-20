package com.example.search.blogsearch.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.search.blogsearch.entity.BlogSearch;
import com.example.search.blogsearch.repository.BlogSearchRepository;
import com.example.search.blogsearch.request.BlogSearchRequest;
import com.example.search.blogsearch.response.BlogSearchResponse;
import com.example.search.blogsearch.response.PopularWordsResponse;
import com.example.search.openapi.BlogSearchApi;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;
import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.response.BlogSearchApiResponse;
import com.example.search.openapi.response.BlogSearchApiResult;

@Transactional
@SpringBootTest
class BlogSearchApiServiceTest {

	BlogSearchApiService blogSearchApiService;

	@Autowired
	BlogSearchService blogSearchService;

	@Autowired
	BlogSearchRepository blogSearchRepository;

	@BeforeEach
	void beforeEach() {
		blogSearchRepository.deleteAll();
	}

	@Test
	@DisplayName("첫 밴째 외부 블로그 검색 API가 서버 오류 리턴시 두 번째 외부 블로그 검색 API 요청")
	void blogSearchApiServiceTest1() {

		// blogSearchService Mock 객체 삽입
		blogSearchService = new BlogSearchService() {
			@Override
			public void updateWordCount(String word) {
			}

			@Override
			public void saveOrUpdateWordCount(String word) {
			}

			@Override
			public PopularWordsResponse getPopularWords() {
				return new PopularWordsResponse(Collections.emptyList());
			}
		};

		// BlogSearchApi Mock 객체 삽입
		List<BlogSearchApi> blogSearchApiList = List.of(
			// 첫 번째 외부 블로그 검색 API가 무조건 예외를 던지도록 설정
			new BlogSearchApi() {
				@Override
				public BlogSearchApiResponse search(BlogSearchApiRequest request) {
					throw new BlogSearchApiServerErrorException();
				}
			},
			new BlogSearchApi() {
				@Override
				public BlogSearchApiResponse search(BlogSearchApiRequest request) {
					return new BlogSearchApiResponse(1, List.of(new BlogSearchApiResult(
						"title",
						"contents",
						"url",
						"blogName",
						LocalDate.of(2023, 1, 1))));
				}
			}
		);

		blogSearchApiService = new BlogSearchApiService(blogSearchService, blogSearchApiList);

		BlogSearchResponse response = blogSearchApiService.search(new BlogSearchRequest("test", null, 1, 10));
		Assertions.assertThat(response.getTotal()).isEqualTo(1);
		Assertions.assertThat(response.getResults().get(0).getTitle()).isEqualTo("title");
		Assertions.assertThat(response.getResults().get(0).getContents()).isEqualTo("contents");
		Assertions.assertThat(response.getResults().get(0).getUrl()).isEqualTo("url");
		Assertions.assertThat(response.getResults().get(0).getBlogName()).isEqualTo("blogName");
		Assertions.assertThat(response.getResults().get(0).getDate()).isEqualTo(LocalDate.of(2023, 1, 1));
	}

	@Test
	@DisplayName("검색 횟수 DB 저장 동시성 테스트")
	void blogSearchApiServiceTest2() throws InterruptedException {

		// Given
		String searchWord = "test";
		int numberOfThreads = 1000;

		// Mock BlogSearchApi 객체 삽입
		blogSearchApiService = new BlogSearchApiService(blogSearchService, List.of(new BlogSearchApi() {
			@Override
			public BlogSearchApiResponse search(BlogSearchApiRequest request) {
				return new BlogSearchApiResponse(0, Collections.emptyList());
			}
		}));

		ExecutorService service = Executors.newFixedThreadPool(1000);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		BlogSearchRequest request = new BlogSearchRequest(searchWord, null, 1, 10);

		for (int i = 0; i < numberOfThreads; i++) {
			service.execute(() -> {
				try {
					blogSearchApiService.search(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
				latch.countDown();
			});
		}

		latch.await();

		Optional<BlogSearch> word = blogSearchRepository.findByWord(request.getWord());
		Assertions.assertThat(word).isNotEmpty();
		Assertions.assertThat(word.get().getWord()).isEqualTo(request.getWord());
		Assertions.assertThat(word.get().getCount()).isEqualTo(numberOfThreads);
	}
}
