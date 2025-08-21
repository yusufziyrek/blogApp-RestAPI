package com.yusufziyrek.blogApp.post.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String text;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int commentCount;
    private int likeCount;
    private String authorUsername;
    private String authorFullName;
}
