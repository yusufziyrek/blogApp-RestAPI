package com.yusufziyrek.blogApp.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    
    private Long id;
    private Long userId;
    private String userFullName;
    private Long postId;
    private String postTitle;
    private Long commentId;
    private LocalDateTime createdDate;
}
