package com.yusufziyrek.blogApp.post.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePostRequest {
    
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;
    
    @Size(min = 10, max = 5000, message = "Text must be between 10 and 5000 characters")
    private String text;
}
