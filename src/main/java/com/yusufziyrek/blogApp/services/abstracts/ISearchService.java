package com.yusufziyrek.blogApp.services.abstracts;

import org.springframework.data.domain.Pageable;

import com.yusufziyrek.blogApp.services.responses.SearchResultResponse;

public interface ISearchService {

	public SearchResultResponse search(String query, String type, Pageable pageable);
}
