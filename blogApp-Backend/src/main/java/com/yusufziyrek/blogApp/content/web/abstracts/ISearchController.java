package com.yusufziyrek.blogApp.content.web.abstracts;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yusufziyrek.blogApp.content.dto.responses.SearchResultResponse;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;

import jakarta.validation.constraints.NotBlank;

@RequestMapping("/api/v1/search")
@Validated
public interface ISearchController {

	@GetMapping
	public ResponseEntity<ApiResponse<SearchResultResponse>> search(@RequestParam("query") @NotBlank(message = "Search query cannot be empty") String query,
			@RequestParam(value = "type", defaultValue = "all") String type, Pageable pageable);

}
