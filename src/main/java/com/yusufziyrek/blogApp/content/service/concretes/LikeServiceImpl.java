package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.domain.models.Comment;
import com.yusufziyrek.blogApp.content.domain.models.Like;
import com.yusufziyrek.blogApp.content.domain.models.Post;
import com.yusufziyrek.blogApp.content.domain.rules.LikeServiceRules;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForCommentRequest;
import com.yusufziyrek.blogApp.content.dto.requests.CreateLikeForPostRequest;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForCommentResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetAllLikesForPostResponse;
import com.yusufziyrek.blogApp.content.dto.responses.GetByIdLikeResponse;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ILikeService;
import com.yusufziyrek.blogApp.identity.domain.models.User;
import com.yusufziyrek.blogApp.shared.exception.CommentException;
import com.yusufziyrek.blogApp.shared.exception.LikeException;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {

    private final ILikeRepository likeRepository;
    private final IPostRepository postRepository;
    private final ICommentRepository commentRepository;
    private final LikeServiceRules serviceRules;

    @Override
    public List<GetAllLikesForPostResponse> getAllForPost(Long postId) {
        return likeRepository.findByPostId(postId).stream()
            .map(like -> GetAllLikesForPostResponse.builder()
                .id(like.getId())
                .userWhoLiked(like.getUser().getUsername())
                .likedPostTitle(like.getPost().getTitle())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<GetAllLikesForCommentResponse> getAllLikesForComment(Long commentId) {
        return likeRepository.findByCommentId(commentId).stream()
            .map(like -> GetAllLikesForCommentResponse.builder()
                .id(like.getId())
                .userWhoLiked(like.getUser().getUsername())
                .likedCommentText(like.getComment().getText())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public GetByIdLikeResponse getById(Long id) {
        Like like = likeRepository.findById(id)
            .orElseThrow(() -> new LikeException("Like not found!"));

        return GetByIdLikeResponse.builder()
            .id(like.getId())
            .username(like.getUser().getUsername())
            .postTitle(like.getPost() != null ? like.getPost().getTitle() : null)
            .commentId(like.getComment() != null ? like.getComment().getId() : null)
            .build();
    }

    @Override
    public Like addLikeForPost(Long postId, CreateLikeForPostRequest req, User user) {
        serviceRules.checkIfLikeAlreadyExistForPost(user.getId(), postId);

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException("Post not found!"));

        Like like = Like.builder()
            .user(user)
            .post(post)
            .build();

        post.incrementLikeCount();
        postRepository.save(post);

        return likeRepository.save(like);
    }

    @Override
    public Like addLikeForComment(Long commentId, CreateLikeForCommentRequest req, User user) {
        serviceRules.checkIfLikeAlreadyExistForComment(user.getId(), commentId);

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentException("Comment not found!"));

        Like like = Like.builder()
            .user(user)
            .comment(comment)
            .build();

        comment.incrementLikeCount();
        commentRepository.save(comment);

        return likeRepository.save(like);
    }

    @Override
    public void dislikeForPost(Long likeId, User user) {
        Like like = likeRepository.findById(likeId)
            .orElseThrow(() -> new LikeException("Like not found!"));

        if (!like.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to remove this like.");
        }

        Post post = like.getPost();
        post.decrementLikeCount();
        postRepository.save(post);

        likeRepository.deleteById(likeId);
    }

    @Override
    public void dislikeForComment(Long likeId, User user) {
        Like like = likeRepository.findById(likeId)
            .orElseThrow(() -> new LikeException("Like not found!"));

        if (!like.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to remove this like.");
        }

        Comment comment = like.getComment();
        comment.decrementLikeCount();
        commentRepository.save(comment);

        likeRepository.deleteById(likeId);
    }
}