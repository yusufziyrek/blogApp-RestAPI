package com.yusufziyrek.blogApp.like.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.like.domain.Like;
import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.shared.exception.UserException;
import com.yusufziyrek.blogApp.shared.exception.PostException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class LikePostUseCase {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    
    public Like execute(Long userId, Long postId) {
        
        // Check if user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException("User not found with id: " + userId));
        
        // Check if post exists
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostException("Post not found with id: " + postId));
        
        // Check if already liked
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("User has already liked this post");
        }
        
        // Create like
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setComment(null); // This is a post like
        
        return likeRepository.save(like);
    }
}
