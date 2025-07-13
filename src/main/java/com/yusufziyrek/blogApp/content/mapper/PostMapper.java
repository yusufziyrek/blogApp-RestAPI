package com.yusufziyrek.blogApp.content.mapper;

import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public GetByIdPostResponse toGetByIdResponse(Post post) {
        if (post == null) return null;
        
        GetByIdPostResponse response = new GetByIdPostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setText(post.getText());
        response.setCreatedDate(post.getCreatedDate());
        response.setUpdatedDate(post.getUpdatedDate());
        response.setCommentCount(post.getCommentCount());
        response.setLikeCount(post.getLikeCount());
        response.setAuthorUser(post.getUser() != null ? post.getUser().getUsername() : null);
        return response;
    }

    public GetAllPostsResponse toGetAllPostsResponse(Post post) {
        if (post == null) return null;
        
        GetAllPostsResponse response = new GetAllPostsResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setText(post.getText());
        response.setCreatedDate(post.getCreatedDate());
        response.setUpdatedDate(post.getUpdatedDate());
        response.setCommentCount(post.getCommentCount());
        response.setLikeCount(post.getLikeCount());
        response.setAuthorUser(post.getUser() != null ? post.getUser().getUsername() : null);
        return response;
    }

    public List<GetAllPostsResponse> toGetAllPostsResponseList(List<Post> posts) {
        if (posts == null) return null;
        
        return posts.stream()
                .map(this::toGetAllPostsResponse)
                .collect(Collectors.toList());
    }

    public Post toPost(CreatePostRequest request) {
        if (request == null) return null;
        
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setCreatedDate(LocalDateTime.now());
        post.setUpdatedDate(LocalDateTime.now());
        return post;
    }

    public void updatePostFromRequest(Post post, UpdatePostRequest request) {
        if (post == null || request == null) return;
        
        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getText() != null) {
            post.setText(request.getText());
        }
        post.setUpdatedDate(LocalDateTime.now());
    }
} 