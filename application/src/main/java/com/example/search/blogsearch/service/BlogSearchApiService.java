package com.example.search.blogsearch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.search.blogsearch.request.BlogSearchRequest;
import com.example.search.blogsearch.response.BlogSearchResponse;
import com.example.search.blogsearch.response.BlogSearchResult;
import com.example.search.blogsearch.response.PopularWordsResponse;
import com.example.search.openapi.BlogSearchApi;
import com.example.search.openapi.exception.BlogSearchApiClientErrorException;
import com.example.search.openapi.exception.BlogSearchApiServerErrorException;
import com.example.search.openapi.request.BlogSearchApiRequest;
import com.example.search.openapi.request.BlogSearchApiSortType;
import com.example.search.openapi.response.BlogSearchApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 블로그 검색 DB 접근, 외부 서비스 API 호출을 처리합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BlogSearchApiService {

	/**
	 * DB에 블로그 검색 키워드, 횟수를 저장하고 인기 검색어 목록을 조회하는 서비스입니다.
	 */
	private final BlogSearchService blogSearchService;

	/**
	 * 외부 블로그 검색 API를 호출하는 구현체 목록입니다.
	 */
	private final List<BlogSearchApi> blogSearchApiList;

	/**
	 * DB에 검색 키워드 검색 횟수를 업데이트하고, 외부 블로그 검색 API를 호출한 결과를 리턴합니다.
	 * @param request 블로그 검색 요청, 검색 키워드, 정렬, 페이지, 사이즈로 구성되어 있습니다.
	 * @return BlogSearchResponse 객체
	 */
	public BlogSearchResponse search(BlogSearchRequest request) {

		String word = request.getWord();

		// 파라미터 유효성 검사
		validateRequest(request);

		// DB 업데이트
		updateSearchWord(word);

		BlogSearchApiSortType sortType;
		if ("accuracy".equals(request.getSort())) {
			sortType = BlogSearchApiSortType.ACCURACY;
		} else if ("recency".equals(request.getSort())) {
			sortType = BlogSearchApiSortType.RECENCY;
		} else {
			sortType = BlogSearchApiSortType.ACCURACY;
		}

		BlogSearchApiRequest apiRequest = new BlogSearchApiRequest(request.getWord(), sortType,
			request.getPage(), request.getSize());

		// 외부 서비스 API 호출
		BlogSearchApiResponse response = requestSearch(apiRequest);

		return new BlogSearchResponse(response.getTotal(), response.getResults().stream()
			.map(BlogSearchResult::of)
			.collect(Collectors.toList()));
	}

	/**
	 * 블로그 검색 요청 파라미터 값을 검사합니다.
	 * @throws BlogSearchApiClientErrorException
	 *          블로그 검색 요청 파라미터가 유효하지 않은 경우,
	 *          페이지가 1보다 작거나 50보다 큰 경우,
	 *          사이즈가 1보다 작거나 50보다 큰 경우
	 * @param request 블로그 검색 요청, 검색 키워드, 정렬, 페이지, 사이즈
	 */
	private void validateRequest(BlogSearchRequest request) {

		boolean error = false;
		StringBuilder sb = new StringBuilder();

		if (request.getPage() == null || request.getPage() < 1 || request.getPage() > 50) {
			error = true;
			sb.append("Invalid page value");
		}

		if (request.getSize() == null || request.getSize() < 1 || request.getSize() > 50) {
			if (error) {
				sb.append(", Invalid size value");
			} else {
				error = true;
				sb.append("Invalid size value");
			}
		}

		if (error) {
			throw new BlogSearchApiClientErrorException("Invalid Parameter", sb.toString());
		}
	}

	/**
	 * DB에 블로그 검색 키워드, 검색 횟수를 업데이트합니다.
	 * 검색 키워드가 DB에 없다면 삽입하고, 있다면 검색 횟수를 1 증가시킵니다.
	 * @param word 검색 키워드
	 */
	private void updateSearchWord(String word) {
		try {
			blogSearchService.saveOrUpdateWordCount(word);
		} catch (DataIntegrityViolationException exception) { // 이미 DB에 존재하는 경우 유니크 제약 조건 예외
			try {
				log.info("Insert word statement failed. Try the Update word statement!");
				blogSearchService.updateWordCount(word);
			} catch (Exception e) {
				log.error("Update word statement failed!", e);
			}
		}
	}

	/**
	 * 외부 블로그 검색 API를 호출하여 검색 결과를 리턴합니다.
	 * @throws BlogSearchApiClientErrorException
	 *          외부 블로그 검색 API 응답이 400 오류인 경우
	 * @throws BlogSearchApiServerErrorException
	 *          모든 외부 블로그 검색 API를 호출하였으나 정상 응답을 얻지 못하고 500 오류가 리턴되는 경우
	 * @param request 블로그 검색 API 요청 파라미터
	 * @return 블로그 검색 API 응답, 총 검색 결과 개수, 검색된 블로그 글 목록
	 */
	private BlogSearchApiResponse requestSearch(BlogSearchApiRequest request) {

		for (BlogSearchApi blogSearchApi : blogSearchApiList) {
			try {
				return blogSearchApi.search(request);
			} catch (BlogSearchApiClientErrorException exception) {
				throw exception;
			} catch (Exception exception) {
				continue;
			}
		}

		throw new BlogSearchApiServerErrorException();
	}

	/**
	 * 인기 검색어 목록을 리턴합니다.
	 * @return 인기 검색어 목록
	 */
	public PopularWordsResponse getPopularWords() {
		return blogSearchService.getPopularWords();
	}
}
