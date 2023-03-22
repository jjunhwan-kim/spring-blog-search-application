package com.example.search.blogsearch.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.search.blogsearch.entity.BlogSearch;

import jakarta.persistence.EntityManager;

@DataJpaTest
class BlogSearchRepositoryTest {

	@Autowired
	BlogSearchRepository blogSearchRepository;

	@Autowired
	EntityManager entityManager;

	@BeforeEach
	void beforeEach() {
		blogSearchRepository.deleteAll();
	}

	@Test
	@DisplayName("검색 키워드 조회")
	void blogSearchRepositoryTest1() {

		// Given
		BlogSearch blogSearch = new BlogSearch("test");
		blogSearchRepository.save(blogSearch);

		// When
		Optional<BlogSearch> foundBlogSearch = blogSearchRepository.findByWord(blogSearch.getWord());

		// Then
		Assertions.assertThat(foundBlogSearch).isNotEmpty();
		Assertions.assertThat(foundBlogSearch.get().getWord()).isEqualTo(blogSearch.getWord());
		Assertions.assertThat(foundBlogSearch.get().getCount()).isEqualTo(blogSearch.getCount());
	}

	@Test
	@DisplayName("검색 키워드 검색 횟수 업데이트")
	void blogSearchRepositoryTest2() {

		// Given
		BlogSearch blogSearch = new BlogSearch("test");
		blogSearchRepository.save(blogSearch);

		// UPDATE 전 INSERT 를 DB에 반영하고 영속성 컨텍스트 초기화
		entityManager.flush();
		entityManager.clear();

		// When
		blogSearchRepository.updateWordCount(blogSearch.getWord());
		Optional<BlogSearch> foundBlogSearch = blogSearchRepository.findByWord(blogSearch.getWord());

		// Then
		Assertions.assertThat(foundBlogSearch).isNotEmpty();
		Assertions.assertThat(foundBlogSearch.get().getWord()).isEqualTo(blogSearch.getWord());
		Assertions.assertThat(foundBlogSearch.get().getCount()).isEqualTo(blogSearch.getCount() + 1);
	}

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 키워드가 10개 미만일 때 전체 키워드 제공")
	void blogSearchRepositoryTest3() {

		// Given
		int numberOfWords = 5;
		List<BlogSearch> blogSearchList = new ArrayList<>();
		for (int i = 1; i <= numberOfWords; i++) {
			blogSearchList.add(new BlogSearch("word" + i, (long)i));
		}
		blogSearchRepository.saveAll(blogSearchList);

		// When
		List<BlogSearch> foundBlogSearchList = blogSearchRepository.findFirst10ByOrderByCountDescWordAsc();

		// Then
		Assertions.assertThat(foundBlogSearchList)
			.extracting("word")
			.containsExactly("word5", "word4", "word3", "word2", "word1");
		Assertions.assertThat(foundBlogSearchList).extracting("count").containsExactly(5L, 4L, 3L, 2L, 1L);
	}

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 키워드가 10개 이상일 때 10개의 키워드 제공")
	void blogSearchRepositoryTest4() {

		// Given
		int numberOfWords = 11;
		List<BlogSearch> blogSearchList = new ArrayList<>();
		for (int i = 1; i <= numberOfWords; i++) {
			blogSearchList.add(new BlogSearch("word" + i, (long)i));
		}
		blogSearchRepository.saveAll(blogSearchList);

		// When
		List<BlogSearch> foundBlogSearchList = blogSearchRepository.findFirst10ByOrderByCountDescWordAsc();

		// Then
		Assertions.assertThat(foundBlogSearchList)
			.extracting("word")
			.containsExactly("word11", "word10", "word9", "word8", "word7", "word6", "word5", "word4", "word3",
				"word2");
		Assertions.assertThat(foundBlogSearchList).extracting("count")
			.containsExactly(11L, 10L, 9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L);
	}

	@Test
	@DisplayName("인기 검색 키워드 목록 조회 - 검색 횟수 내림차순, 검색어 오름차순 정렬, 검색 횟수 같을 때 알파벳 오름차순 정렬")
	void blogSearchRepositoryTest5() {

		// Given
		blogSearchRepository.save(new BlogSearch("a"));
		blogSearchRepository.save(new BlogSearch("A"));
		blogSearchRepository.save(new BlogSearch("word1"));
		blogSearchRepository.save(new BlogSearch("word2"));

		// When
		List<BlogSearch> foundBlogSearchList = blogSearchRepository.findFirst10ByOrderByCountDescWordAsc();

		// Then
		Assertions.assertThat(foundBlogSearchList)
			.extracting("word")
			.containsExactly("A", "a", "word1", "word2");
		Assertions.assertThat(foundBlogSearchList).extracting("count")
			.containsExactly(1L, 1L, 1L, 1L);
	}
}