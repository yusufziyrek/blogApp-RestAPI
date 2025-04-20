package com.yusufziyrek.blogApp.content.web.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.content.dto.responses.SearchResultResponse;
import com.yusufziyrek.blogApp.content.service.concretes.SearchServiceImpl;
import com.yusufziyrek.blogApp.content.web.abstracts.ISearchController;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

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
