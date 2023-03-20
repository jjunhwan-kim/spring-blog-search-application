package com.example.search.blogsearch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.search.blogsearch.entity.BlogSearch;
import com.example.search.blogsearch.repository.BlogSearchRepository;
import com.example.search.blogsearch.response.PopularWord;
import com.example.search.blogsearch.response.PopularWordsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * DB에 블로그 검색 키워드, 횟수를 저장하고 인기 검색어 목록을 조회합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchServiceImpl implements BlogSearchService{

	/**
	 * JpaRepository 구현체입니다.
	 */
	private final BlogSearchRepository blogSearchRepository;

	/**
	 * 검색 키워드의 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	@Override
	@Transactional
	public void updateWordCount(String word) {
		blogSearchRepository.updateWordCount(word);
	}

	/**
	 * 검색 키워드가 DB에 없다면 삽입하고, 있다면 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	@Override
	@Transactional
	public void saveOrUpdateWordCount(String word) {
		Optional<BlogSearch> optionalBlogSearch = blogSearchRepository.findByWord(word);

		if (optionalBlogSearch.isEmpty()) {
			blogSearchRepository.save(new BlogSearch(word));
		} else {
			blogSearchRepository.updateWordCount(word);
		}
	}

	/**
	 * 인기 검색어 목록을 조회합니다.
	 * @return 인기 검색어 목록, 검색 키워드와 검색 횟수
	 */
	@Override
	@Transactional(readOnly = true)
	public PopularWordsResponse getPopularWords() {

		List<BlogSearch> blogSearchList = blogSearchRepository.findFirst10ByOrderByCountDescWordAsc();

		return new PopularWordsResponse(blogSearchList.stream()
			.map(PopularWord::of)
			.collect(Collectors.toList()));
	}
}
