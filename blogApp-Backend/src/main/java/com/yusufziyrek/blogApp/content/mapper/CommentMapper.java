package com.yusufziyrek.blogApp.content.mapper;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.dto.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdCommentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public GetByIdCommentResponse toGetByIdResponse(Comment comment) {
        if (comment == null) return null;
        
        GetByIdCommentResponse response = new GetByIdCommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        return response;
    }

    public GetAllCommentsForPostResponse toGetAllCommentsForPostResponse(Comment comment) {
        if (comment == null) return null;
        
        GetAllCommentsForPostResponse response = new GetAllCommentsForPostResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        response.setPostTitle(comment.getPost() != null ? comment.getPost().getTitle() : null);
        response.setAuthorUser(comment.getUser() != null ? comment.getUser().getUsername() : null);
        return response;
    }

    public GetAllCommentsForUserResponse toGetAllCommentsForUserResponse(Comment comment) {
        if (comment == null) return null;
        
        GetAllCommentsForUserResponse response = new GetAllCommentsForUserResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        return response;
    }

    public List<GetAllCommentsForPostResponse> toGetAllCommentsForPostResponseList(List<Comment> comments) {
        if (comments == null) return null;
        
        return comments.stream()
                .map(this::toGetAllCommentsForPostResponse)
                .collect(Collectors.toList());
    }

    public List<GetAllCommentsForUserResponse> toGetAllCommentsForUserResponseList(List<Comment> comments) {
        if (comments == null) return null;
        
        return comments.stream()
                .map(this::toGetAllCommentsForUserResponse)
                .collect(Collectors.toList());
    }

    public Comment toComment(CreateCommentRequest request) {
        if (request == null) return null;
        
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setCreatedDate(LocalDateTime.now());
        return comment;
    }

    public void updateCommentFromRequest(Comment comment, UpdateCommentRequest request) {
        if (comment == null || request == null) return;
        
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
    }
} 