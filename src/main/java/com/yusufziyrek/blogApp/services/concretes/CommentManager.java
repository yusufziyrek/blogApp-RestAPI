package com.yusufziyrek.blogApp.services.concretes;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.entities.Comment;
import com.yusufziyrek.blogApp.entities.Post;
import com.yusufziyrek.blogApp.repos.ICommentRepository;
import com.yusufziyrek.blogApp.repos.IPostRepository;
import com.yusufziyrek.blogApp.repos.IUserRepository;
import com.yusufziyrek.blogApp.services.abstracts.ICommentService;
import com.yusufziyrek.blogApp.services.requests.CreateCommentRequest;
import com.yusufziyrek.blogApp.services.requests.UpdateCommentRequest;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForPostResponse;
import com.yusufziyrek.blogApp.services.responses.GetAllCommentsForUserResponse;
import com.yusufziyrek.blogApp.services.responses.GetByIdCommentResponse;
import com.yusufziyrek.blogApp.utilites.mappers.IModelMapperService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentManager implements ICommentService {

    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IModelMapperService modelMapperService;

    @Override
    @Cacheable(value = "commentsForPost", key = "#postId")
    public List<GetAllCommentsForPostResponse> getAllForPost(Long postId) {
        List<Comment> comments = this.commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> {
            GetAllCommentsForPostResponse commentResponse = this.modelMapperService
                    .forResponse().map(comment, GetAllCommentsForPostResponse.class);
            commentResponse.setPostTitle(comment.getPost().getTitle());
            commentResponse.setAuthorUser(comment.getUser().getUsername());
            return commentResponse;
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "commentsForUser", key = "#userId")
    public List<GetAllCommentsForUserResponse> getAllForUser(Long userId) {
        List<Comment> comments = this.commentRepository.findByUserId(userId);
        return comments.stream()
                .map(comment -> this.modelMapperService.forResponse().map(comment, GetAllCommentsForUserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "commentDetails", key = "#id")
    public GetByIdCommentResponse getById(Long id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        return this.modelMapperService.forResponse().map(comment, GetByIdCommentResponse.class);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentsForPost", key = "#postId"),
        @CacheEvict(value = "commentsForUser", key = "#createCommentRequest.userId")
    })
    public Comment add(Long postId, CreateCommentRequest createCommentRequest) {
        Comment comment = new Comment();
        comment.setText(createCommentRequest.getText());
        comment.setUser(this.userRepository.findById(createCommentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!")));
        comment.setPost(this.postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found!")));
        comment.setCreatedDate(new Date());

        Post post = comment.getPost();
        post.incrementCommentCount();
        this.postRepository.save(post);

        return this.commentRepository.save(comment);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentDetails", key = "#id"),
        @CacheEvict(value = "commentsForPost", allEntries = true),
        @CacheEvict(value = "commentsForUser", allEntries = true)
    })
    public Comment update(Long id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        comment.setText(updateCommentRequest.getText());
        return this.commentRepository.save(comment);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "commentDetails", key = "#id"),
        @CacheEvict(value = "commentsForPost", allEntries = true),
        @CacheEvict(value = "commentsForUser", allEntries = true)
    })
    public void delete(Long id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        Post post = comment.getPost();
        post.decrementCommentCount();
        this.postRepository.save(post);
        this.commentRepository.deleteById(id);
    }
}