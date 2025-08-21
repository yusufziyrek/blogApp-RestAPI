package com.yusufziyrek.blogApp.comment.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yusufziyrek.blogApp.comment.domain.Comment;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.post.domain.Post;
import com.yusufziyrek.blogApp.user.domain.User;
import com.yusufziyrek.blogApp.shared.exception.PostException;
import com.yusufziyrek.blogApp.shared.exception.UserException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateCommentUseCase {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public Comment execute(String text, Long postId, Long userId) {
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found"));
        
        // Validate post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found"));
        
        // Create comment
        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(user);
        comment.setPost(post);
        
        // Use domain logic for validation and publishing
        comment.publish();
        
        // Save comment
        Comment savedComment = commentRepository.save(comment);
        
        // Update post comment count using domain logic
        post.incrementCommentCount();
        postRepository.save(post);
        
        return savedComment;
    }
}
