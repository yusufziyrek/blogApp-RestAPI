package com.yusufziyrek.blogApp.controllers.concretes;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.yusufziyrek.blogApp.services.concretes.SearchService;
import com.yusufziyrek.blogApp.services.responses.ApiResponse;
import com.yusufziyrek.blogApp.services.responses.SearchResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<SearchResultResponse>> search(
            @RequestParam("query") String query,
            @RequestParam(value = "type", defaultValue = "all") String type,
            Pageable pageable) {
                
        SearchResultResponse result = searchService.search(query, type, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Arama başarılı", result));
    }
}
