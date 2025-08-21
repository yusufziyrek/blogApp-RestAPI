package com.yusufziyrek.blogApp.post.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CreatePostUseCase {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public Post execute(String title, String text, Long userId) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found"));
        
        // Create post
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setUser(user);
        
        // Use domain logic for validation and publishing
        post.publish();
        
        // Save post
        return postRepository.save(post);
    }
}
