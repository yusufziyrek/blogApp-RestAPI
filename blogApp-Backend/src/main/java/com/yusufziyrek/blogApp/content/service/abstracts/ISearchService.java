package com.yusufziyrek.blogApp.content.service.abstracts;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.content.dto.responses.SearchResultResponse;

public interface ISearchService {

	public SearchResultResponse search(String query, String type, Pageable pageable);
}
