package com.yusufziyrek.blogApp.like.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.application.usecases.LikePostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.LikeCommentUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetPostLikesUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetCommentLikesUseCase;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;

/**
 * Spring Configuration for Like Module
 * Wires up all use cases with their dependencies following Clean Architecture
 */
@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.like.infrastructure"
})
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.like.infrastructure.persistence")
@EntityScan(basePackages = "com.yusufziyrek.blogApp.like.infrastructure.persistence")
public class LikeModuleConfiguration {
    
    @Bean
    public LikePostUseCase likePostUseCase(
            LikeRepository likeRepository,
            com.yusufziyrek.blogApp.user.application.ports.UserRepository userRepository,
            PostRepository postRepository) {
        return new LikePostUseCase(likeRepository, userRepository, postRepository);
    }
    
    @Bean
    public LikeCommentUseCase likeCommentUseCase(
            LikeRepository likeRepository,
            com.yusufziyrek.blogApp.user.application.ports.UserRepository userRepository,
            CommentRepository commentRepository) {
        return new LikeCommentUseCase(likeRepository, userRepository, commentRepository);
    }
    
    @Bean
    public UnlikeUseCase unlikeUseCase(
            LikeRepository likeRepository,
            PostRepository postRepository,
            CommentRepository commentRepository) {
        return new UnlikeUseCase(likeRepository, postRepository, commentRepository);
    }
    
    @Bean
    public GetPostLikesUseCase getPostLikesUseCase(LikeRepository likeRepository) {
        return new GetPostLikesUseCase(likeRepository);
    }
    
    @Bean
    public GetCommentLikesUseCase getCommentLikesUseCase(LikeRepository likeRepository) {
        return new GetCommentLikesUseCase(likeRepository);
    }
}
