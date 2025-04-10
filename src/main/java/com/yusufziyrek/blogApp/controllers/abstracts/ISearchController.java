package com.yusufziyrek.blogApp.controllers.abstracts;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.SearchResultResponse;

@RequestMapping("/api/v1/search")
public interface ISearchController {

	@GetMapping
	public ResponseEntity<ApiResponse<SearchResultResponse>> search(@RequestParam("query") String query,
			@RequestParam(value = "type", defaultValue = "all") String type, Pageable pageable);

}
