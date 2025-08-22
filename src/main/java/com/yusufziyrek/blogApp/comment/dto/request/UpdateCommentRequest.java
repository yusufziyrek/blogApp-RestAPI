package com.yusufziyrek.blogApp.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCommentRequest {
    
    @NotBlank(message = "CommentDomain text cannot be blank")
    @Size(min = 1, max = 1000, message = "CommentDomain text must be between 1 and 1000 characters")
    private String text;
}
