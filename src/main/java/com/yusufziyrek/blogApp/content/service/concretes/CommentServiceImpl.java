package com.yusufziyrek.blogApp.content.service.concretes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdCommentResponse;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ICommentService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(keyGenerator = "cacheKeyGenerator")
public class CommentServiceImpl implements ICommentService {

    private final ICommentRepository commentRepository;
    private final IPostRepository postRepository;

    @Override
    @Cacheable(value = "commentsForPost")
    public List<GetAllCommentsForPostResponse> getAllForPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
            .map(comment -> GetAllCommentsForPostResponse.builder()
                .id(comment.getId())
                .postTitle(comment.getPost().getTitle())
                .authorUser(comment.getUser().getUsername())
                .text(comment.getText())
                .likeCount(comment.getLikeCount())
                .createDate(Date.from(
                    comment.getCreatedDate()
                           .atZone(ZoneId.systemDefault())
                           .toInstant()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "commentsForUser")
    public List<GetAllCommentsForUserResponse> getAllForUser(Long userId) {
        return commentRepository.findByUserId(userId).stream()
            .map(comment -> GetAllCommentsForUserResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .text(comment.getText())
                .likeCount(comment.getLikeCount())
                .createDate(Date.from(
                    comment.getCreatedDate()
                           .atZone(ZoneId.systemDefault())
                           .toInstant()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "commentDetails", key = "#id")
    public GetByIdCommentResponse getById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new CommentException("Comment not found!"));

        return GetByIdCommentResponse.builder()
            .id(comment.getId())
            .postId(comment.getPost().getId())
            .userId(comment.getUser().getId())
            .text(comment.getText())
            .likeCount(comment.getLikeCount())
            .createdDate(Date.from(
                comment.getCreatedDate()
                       .atZone(ZoneId.systemDefault())
                       .toInstant()))
            .build();
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentsForPost", key = "#postId"),
        @CacheEvict(value = "commentsForUser", key = "#user.id")
    })
    public Comment add(Long postId, CreateCommentRequest req, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException("Post not found!"));

        Comment comment = Comment.builder()
            .text(req.getText())
            .user(user)
            .post(post)
            .createdDate(LocalDateTime.now())
            .build();

        post.incrementCommentCount();
        postRepository.save(post);

        return commentRepository.save(comment);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentDetails", key = "#id"),
        @CacheEvict(value = "commentsForPost", allEntries = true),
        @CacheEvict(value = "commentsForUser", allEntries = true)
    })
    public Comment update(Long id, UpdateCommentRequest req, User user) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new CommentException("Comment not found!"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to update this comment.");
        }

        comment.setText(req.getText());
        return commentRepository.save(comment);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentDetails", key = "#id"),
        @CacheEvict(value = "commentsForPost", allEntries = true),
        @CacheEvict(value = "commentsForUser", allEntries = true)
    })
    public void delete(Long id, User user) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new CommentException("Comment not found!"));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this comment.");
        }

        Post post = comment.getPost();
        post.decrementCommentCount();
        postRepository.save(post);

        commentRepository.deleteById(id);
    }
}