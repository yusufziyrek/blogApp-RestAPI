package com.yusufziyrek.blogApp.post.application.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.yusufziyrek.blogApp.post.application.usecases.*;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.LikePostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetPostLikesUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUsersByIdsUseCase;
import com.yusufziyrek.blogApp.post.domain.PostDomain;
import com.yusufziyrek.blogApp.comment.domain.CommentDomain;
import com.yusufziyrek.blogApp.post.dto.request.CreatePostRequest;
import com.yusufziyrek.blogApp.post.dto.request.UpdatePostRequest;
import com.yusufziyrek.blogApp.post.dto.response.PostResponse;
import com.yusufziyrek.blogApp.comment.dto.request.CreateCommentRequest;
import com.yusufziyrek.blogApp.comment.dto.response.CommentResponse;
import com.yusufziyrek.blogApp.like.domain.LikeDomain;
import com.yusufziyrek.blogApp.like.dto.response.LikeResponse;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;
import com.yusufziyrek.blogApp.post.infrastructure.mappers.PostMapper;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PostApplicationService {

    // Post related use cases
    private final CreatePostUseCase createPostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetAllPostsUseCase getAllPostsUseCase;
    private final GetPostByIdUseCase getPostByIdUseCase;
    private final GetPostsByUserIdUseCase getPostsByUserIdUseCase;
    private final UpdatePostUseCase updatePostUseCase;

    // Cross-module use cases
    private final CreateCommentUseCase createCommentUseCase;
    private final GetCommentsForPostUseCase getCommentsForPostUseCase;
    private final LikePostUseCase likePostUseCase;
    private final UnlikeUseCase unlikeUseCase;
    private final GetPostLikesUseCase getPostLikesUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUsersByIdsUseCase getUsersByIdsUseCase;

    private final PostMapper postMapper;

    public PostApplicationService(CreatePostUseCase createPostUseCase, DeletePostUseCase deletePostUseCase,
            GetAllPostsUseCase getAllPostsUseCase, GetPostByIdUseCase getPostByIdUseCase,
            GetPostsByUserIdUseCase getPostsByUserIdUseCase, UpdatePostUseCase updatePostUseCase,
            CreateCommentUseCase createCommentUseCase, GetCommentsForPostUseCase getCommentsForPostUseCase,
            LikePostUseCase likePostUseCase, UnlikeUseCase unlikeUseCase,
            GetPostLikesUseCase getPostLikesUseCase, GetUserByIdUseCase getUserByIdUseCase,
            GetUsersByIdsUseCase getUsersByIdsUseCase,
            PostMapper postMapper) {
        this.createPostUseCase = createPostUseCase;
        this.deletePostUseCase = deletePostUseCase;
        this.getAllPostsUseCase = getAllPostsUseCase;
        this.getPostByIdUseCase = getPostByIdUseCase;
        this.getPostsByUserIdUseCase = getPostsByUserIdUseCase;
        this.updatePostUseCase = updatePostUseCase;
        this.createCommentUseCase = createCommentUseCase;
        this.getCommentsForPostUseCase = getCommentsForPostUseCase;
        this.likePostUseCase = likePostUseCase;
        this.unlikeUseCase = unlikeUseCase;
        this.getPostLikesUseCase = getPostLikesUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getUsersByIdsUseCase = getUsersByIdsUseCase;
        this.postMapper = postMapper;
    }

    public PostResponse createPost(CreatePostRequest request, Long userId) {
        PostDomain post = createPostUseCase.execute(request, userId);
        UserResponse user = getUserByIdUseCase.execute(userId);
        String fullName = user.getFirstname() + " " + user.getLastname();
        return postMapper.toResponse(post, user.getUsername(), fullName);
    }

    public List<PostResponse> getAllPosts(int page, int size) {
        PageResponse<PostDomain> pageResponse = getAllPostsUseCase.execute(page, size);

        // Batch fetch: Collect all user IDs and fetch in single query to avoid N+1
        Set<Long> userIds = pageResponse.getItems().stream()
                .map(PostDomain::getUserId)
                .collect(Collectors.toSet());

        Map<Long, UserResponse> userMap = getUsersByIdsUseCase.execute(userIds);

        return pageResponse.getItems().stream()
                .map(postDomain -> {
                    UserResponse user = userMap.get(postDomain.getUserId());
                    String fullName = (user != null)
                            ? user.getFirstname() + " " + user.getLastname()
                            : "Unknown User";
                    String username = (user != null) ? user.getUsername() : "unknown";
                    return postMapper.toResponse(postDomain, username, fullName);
                })
                .collect(Collectors.toList());
    }

    public List<PostResponse> getCurrentUserPosts(Long userId, String username, int page, int size) {
        PageResponse<PostDomain> pageResponse = getPostsByUserIdUseCase.execute(userId, page, size);

        return pageResponse.getItems().stream()
                .map(postDomain -> postMapper.toResponse(postDomain, username, username))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        PostDomain post = getPostByIdUseCase.execute(id);
        UserResponse user = getUserByIdUseCase.execute(post.getUserId());
        String fullName = user.getFirstname() + " " + user.getLastname();
        return postMapper.toResponse(post, user.getUsername(), fullName);
    }

    public PostResponse updatePost(Long id, UpdatePostRequest request, Long userId) {
        PostDomain post = updatePostUseCase.execute(id, request.getTitle(), request.getText(), userId);
        UserResponse user = getUserByIdUseCase.execute(userId);
        String fullName = user.getFirstname() + " " + user.getLastname();
        return postMapper.toResponse(post, user.getUsername(), fullName);
    }

    public void deletePost(Long id, Long userId) {
        deletePostUseCase.execute(id, userId);
    }

    // Cross-module coordination methods
    public CommentResponse createCommentForPost(Long postId, CreateCommentRequest request, Long userId) {
        request.setPostId(postId);
        return createCommentUseCase.execute(request, userId);
    }

    public List<CommentResponse> getCommentsForPost(Long postId, int page, int size) {
        PageResponse<CommentDomain> pageResponse = getCommentsForPostUseCase.execute(postId, page, size);
        PostDomain post = getPostByIdUseCase.execute(postId);
        String postTitle = post.getTitle();

        return pageResponse.getItems().stream()
                .map(comment -> mapToCommentResponse(comment, postTitle))
                .collect(Collectors.toList());
    }

    public LikeResponse likePost(Long postId, Long userId) {
        LikeDomain like = likePostUseCase.execute(userId, postId);
        return mapToLikeResponse(like);
    }

    public void unlikePost(Long postId, Long userId) {
        unlikeUseCase.executeForPost(userId, postId);
    }

    public List<LikeResponse> getPostLikes(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LikeDomain> likes = getPostLikesUseCase.execute(postId, pageable);

        return likes.getContent().stream()
                .map(this::mapToLikeResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToCommentResponse(CommentDomain comment, String postTitle) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setLikeCount(comment.getLikeCount());
        response.setCreatedDate(comment.getCreatedDate());
        response.setUpdatedDate(comment.getUpdatedDate());
        response.setPostId(comment.getPostId());
        response.setPostTitle(postTitle);

        try {
            UserResponse user = getUserByIdUseCase.execute(comment.getUserId());
            response.setAuthorUsername(user.getUsername());
            response.setAuthorFullName(user.getFirstname() + " " + user.getLastname());
        } catch (Exception ex) {
            response.setAuthorUsername("Unknown");
            response.setAuthorFullName("Unknown User");
        }

        return response;
    }

    private LikeResponse mapToLikeResponse(LikeDomain like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());
        response.setUserId(like.getUserId());
        response.setPostId(like.getPostId());
        response.setCreatedDate(like.getCreatedDate());
        try {
            UserResponse user = getUserByIdUseCase.execute(like.getUserId());
            response.setUserFullName(user.getFirstname() + " " + user.getLastname());
        } catch (Exception ex) {
            response.setUserFullName("Unknown User");
        }
        if (like.getPostId() != null) {
            try {
                PostDomain post = getPostByIdUseCase.execute(like.getPostId());
                response.setPostTitle(post.getTitle());
            } catch (Exception ex) {
                response.setPostTitle("Unknown Post");
            }
        }
        return response;
    }
}
