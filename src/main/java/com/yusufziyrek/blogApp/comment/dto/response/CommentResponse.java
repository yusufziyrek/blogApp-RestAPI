package com.yusufziyrek.blogApp.comment.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String text;
    private int likeCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String authorUsername;
    private String authorFullName;
    private Long postId;
    private String postTitle;
}
