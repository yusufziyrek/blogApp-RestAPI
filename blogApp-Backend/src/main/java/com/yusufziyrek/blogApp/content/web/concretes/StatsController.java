package com.yusufziyrek.blogApp.content.web.concretes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.content.service.concretes.StatsService;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = statsService.getStats();
        return ResponseEntity.ok(new ApiResponse<>(true, "İstatistikler başarıyla getirildi", stats));
    }
} 