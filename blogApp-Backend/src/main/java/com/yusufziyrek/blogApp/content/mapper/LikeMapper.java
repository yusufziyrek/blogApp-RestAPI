package com.yusufziyrek.blogApp.content.mapper;

import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeMapper {

    public GetByIdLikeResponse toGetByIdResponse(Like like) {
        if (like == null) return null;
        
        GetByIdLikeResponse response = new GetByIdLikeResponse();
        response.setId(like.getId());
        response.setUsername(like.getUser() != null ? like.getUser().getUsername() : null);
        return response;
    }

    public GetAllLikesForPostResponse toGetAllLikesForPostResponse(Like like) {
        if (like == null) return null;
        
        GetAllLikesForPostResponse response = new GetAllLikesForPostResponse();
        response.setId(like.getId());
        response.setUsername(like.getUser() != null ? like.getUser().getUsername() : null);
        response.setUserId(like.getUser() != null ? like.getUser().getId() : null);
        return response;
    }

    public GetAllLikesForCommentResponse toGetAllLikesForCommentResponse(Like like) {
        if (like == null) return null;
        
        GetAllLikesForCommentResponse response = new GetAllLikesForCommentResponse();
        response.setId(like.getId());
        response.setUsername(like.getUser() != null ? like.getUser().getUsername() : null);
        return response;
    }

    public List<GetAllLikesForPostResponse> toGetAllLikesForPostResponseList(List<Like> likes) {
        if (likes == null) return null;
        
        return likes.stream()
                .map(this::toGetAllLikesForPostResponse)
                .collect(Collectors.toList());
    }

    public List<GetAllLikesForCommentResponse> toGetAllLikesForCommentResponseList(List<Like> likes) {
        if (likes == null) return null;
        
        return likes.stream()
                .map(this::toGetAllLikesForCommentResponse)
                .collect(Collectors.toList());
    }
} 