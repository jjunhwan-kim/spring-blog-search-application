package com.example.search.blogsearch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 블로그 검색 키워드와 검색 횟수를 저장하는 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BlogSearch {

	/**
	 * 식별자
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 검색 키워드
	 */
	@Column(unique = true)
	private String word;

	/**
	 * 검색 횟수
	 */
	private Long count;

	/**
	 * 검색 키워드를 등록합니다.
	 * @param word 검색 키워드
	 */
	public BlogSearch(String word) {
		this.word = word;
		this.count = 1L;
	}

	/**
	 * 검색 키워드를 등록합니다.
	 * @param word 검색 키워드
	 * @param count 검색 횟수
	 */
	public BlogSearch(String word, Long count) {
		this.word = word;
		this.count = count;
	}
}
