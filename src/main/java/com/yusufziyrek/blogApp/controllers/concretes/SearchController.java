package com.yusufziyrek.blogApp.controllers.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.controllers.abstracts.ISearchController;
import com.yusufziyrek.blogApp.services.concretes.SearchServiceImpl;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.SearchResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController implements ISearchController {

	private final SearchServiceImpl searchService;

	@Override
	public ResponseEntity<ApiResponse<SearchResultResponse>> search(@RequestParam("query") String query,
			@RequestParam(value = "type", defaultValue = "all") String type, Pageable pageable) {

		SearchResultResponse result = searchService.search(query, type, pageable);
		return ResponseEntity.ok(new ApiResponse<>(true, "Arama başarılı", result));
	}
}
