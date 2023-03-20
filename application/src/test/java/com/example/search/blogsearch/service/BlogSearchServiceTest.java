package com.example.search.blogsearch.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.search.blogsearch.entity.BlogSearch;
import com.example.search.blogsearch.repository.BlogSearchRepository;
import com.example.search.blogsearch.response.PopularWordsResponse;

@Transactional
@SpringBootTest
class BlogSearchServiceTest {

	@Autowired
	BlogSearchService blogSearchService;

	@Autowired
	BlogSearchRepository blogSearchRepository;

	@BeforeEach
	void beforeEach() {
		blogSearchRepository.deleteAll();
	}

	Comparator<BlogSearch> comparator = (o1, o2) -> {
		if (Objects.equals(o1.getCount(), o2.getCount())) {
			return o1.getWord().compareTo(o2.getWord());   // asc
		} else {
			return o2.getCount().compareTo(o1.getCount()); // desc
		}
	};

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 키워드가 10개 미만일 때 전체 키워드 제공")
	void getPopularWordsTest1() {

		// Given
		int numberOfWords = 5;
		List<BlogSearch> blogSearchList = new ArrayList<>();
		for (int i = 1; i <= numberOfWords; i++) {
			blogSearchList.add(new BlogSearch("word" + i, (long)i));
		}
		blogSearchRepository.saveAll(blogSearchList);
		blogSearchList.sort(comparator);

		// When
		PopularWordsResponse response = blogSearchService.getPopularWords();

		// Then
		Assertions.assertThat(response.getWords().size()).isEqualTo(numberOfWords);
		for (int i = 0; i < response.getWords().size(); i++) {
			Assertions.assertThat(response.getWords().get(i).getWord()).isEqualTo(blogSearchList.get(i).getWord());
			Assertions.assertThat(response.getWords().get(i).getCount()).isEqualTo(blogSearchList.get(i).getCount());
		}
	}

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 키워드가 10개 이상일 때 10개의 키워드 제공")
	void getPopularWordsTest2() {

		// Given
		int numberOfWords = 20;
		List<BlogSearch> blogSearchList = new ArrayList<>();
		for (int i = 1; i <= numberOfWords; i++) {
			blogSearchList.add(new BlogSearch("word" + i, (long)i));
		}
		blogSearchRepository.saveAll(blogSearchList);
		blogSearchList.sort(comparator);

		// When
		PopularWordsResponse response = blogSearchService.getPopularWords();

		// Then
		Assertions.assertThat(response.getWords().size()).isEqualTo(10);
		for (int i = 0; i < response.getWords().size(); i++) {
			Assertions.assertThat(response.getWords().get(i).getWord()).isEqualTo(blogSearchList.get(i).getWord());
			Assertions.assertThat(response.getWords().get(i).getCount()).isEqualTo(blogSearchList.get(i).getCount());
		}
	}

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 횟수 같을 때 알파벳 오름차순 정렬")
	void getPopularWordsTest3() {

		// Given
		List<BlogSearch> blogSearchList = new ArrayList<>();

		blogSearchRepository.save(new BlogSearch("a"));
		blogSearchRepository.save(new BlogSearch("A"));

		// When
		PopularWordsResponse response = blogSearchService.getPopularWords();

		// Then
		Assertions.assertThat(response.getWords().size()).isEqualTo(2);
		Assertions.assertThat(response.getWords().get(0).getWord()).isEqualTo("A");
		Assertions.assertThat(response.getWords().get(1).getWord()).isEqualTo("a");
	}
}