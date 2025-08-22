package com.yusufziyrek.blogApp.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentRequest {
    
    @NotBlank(message = "CommentDomain text cannot be blank")
    @Size(min = 1, max = 1000, message = "CommentDomain text must be between 1 and 1000 characters")
    private String text;
    
    @NotNull(message = "PostDomain ID is required")
    @Positive(message = "PostDomain ID must be positive")
    private Long postId;
}
