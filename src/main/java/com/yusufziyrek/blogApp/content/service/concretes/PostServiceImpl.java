package com.yusufziyrek.blogApp.content.service.concretes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.dto.requests.CreatePostRequest;
import com.yusufziyrek.blogApp.content.dto.requests.UpdatePostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdPostResponse;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.IPostService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.dto.PageResponse;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(keyGenerator = "cacheKeyGenerator")
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;

    @Override
    @Cacheable(value = "allPosts")
    public PageResponse<GetAllPostsResponse> getAll(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        List<GetAllPostsResponse> items = page.getContent().stream()
            .map(post -> GetAllPostsResponse.builder()
                .id(post.getId())
                .authorUser(post.getUser().getUsername())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .createdDate(post.getCreatedDate())
                .build())
            .collect(Collectors.toList());

        return new PageResponse<>(
            items,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    @Cacheable(value = "userPosts")
    public PageResponse<GetAllPostsResponse> getAllForUser(Pageable pageable, Long userId) {
        Page<Post> page = postRepository.findAllByUserId(pageable, userId);
        List<GetAllPostsResponse> items = page.getContent().stream()
            .map(post -> GetAllPostsResponse.builder()
                .id(post.getId())
                .authorUser(post.getUser().getUsername())
                .title(post.getTitle())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .createdDate(post.getCreatedDate())
                .build())
            .collect(Collectors.toList());

        return new PageResponse<>(
            items,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

    @Override
    @Cacheable(value = "postDetails", key = "#id")
    public GetByIdPostResponse getById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostException("Post id not exist !"));

        return GetByIdPostResponse.builder()
            .id(post.getId())
            .authorUser(post.getUser().getUsername())
            .title(post.getTitle())
            .text(post.getText())
            .commentCount(post.getCommentCount())
            .likeCount(post.getLikeCount())
            .createdDate(post.getCreatedDate())
            .build();
    }

    @Override
    @Cacheable(value = "userPostTitles", key = "#userId")
    public List<String> getPostTitleForUser(Long userId) {
        return postRepository.findByUserId(userId)
            .stream()
            .map(Post::getTitle)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "allPosts", allEntries = true),
        @CacheEvict(value = "userPosts", allEntries = true),
        @CacheEvict(value = "userPostTitles", key = "#user.id")
    })
    public Post createPost(CreatePostRequest req, User user) {
        Post toSave = Post.builder()
            .user(user)
            .title(req.getTitle())
            .text(req.getText())
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        return postRepository.save(toSave);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "postDetails", key = "#id"),
        @CacheEvict(value = "allPosts", allEntries = true),
        @CacheEvict(value = "userPosts", allEntries = true),
        @CacheEvict(value = "userPostTitles", key = "#user.id")
    })
    public Post update(Long id, UpdatePostRequest req, User user) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostException("Post id not exist !"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to update this post.");
        }

        post.setTitle(req.getTitle());
        post.setText(req.getText());
        post.setUpdatedDate(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "postDetails", key = "#id"),
        @CacheEvict(value = "allPosts", allEntries = true),
        @CacheEvict(value = "userPosts", allEntries = true),
        @CacheEvict(value = "userPostTitles", key = "#user.id")
    })
    public void delete(Long id, User user) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostException("Post id not exist !"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this post.");
        }

        postRepository.deleteById(id);
    }
}