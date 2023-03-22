package com.example.search.blogsearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.search.blogsearch.entity.BlogSearch;

import jakarta.persistence.LockModeType;

/**
 * 블로그 검색 요청을 데이터베이스에 저장합니다.
 */
public interface BlogSearchRepository extends JpaRepository<BlogSearch, Long> {

	/**
	 * 검색 키워드로 블로그 검색을 조회합니다.
	 * @param word 검색 키워드
	 * @return BlogSearch 객체
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<BlogSearch> findByWord(String word);

	/**
	 * 검색 키워드의 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	@Modifying
	@Query("UPDATE BlogSearch bs SET bs.count = bs.count + 1 WHERE bs.word = :word")
	void updateWordCount(@Param("word") String word);

	/**
	 * 인기 검색어 목록을 리턴합니다.
	 * @return 검색 횟수 내림차순, 검색 키워드 오름차순으로 정렬하여 상위 10개의 검색 키워드를 리턴합니다.
	 */
	List<BlogSearch> findFirst10ByOrderByCountDescWordAsc();
}
