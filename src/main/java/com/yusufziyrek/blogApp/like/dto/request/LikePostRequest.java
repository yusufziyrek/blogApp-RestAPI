package com.yusufziyrek.blogApp.like.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikePostRequest {
    
    @NotNull(message = "Post ID is required")
    private Long postId;
}
