package com.yusufziyrek.blogApp.post.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostsByUserIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostsByUserIdUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCaseImpl;

/**
 * Configuration for PostDomain Module
 * Clean Architecture - Infrastructure Layer
 */
@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.post.infrastructure.persistence",
    "com.yusufziyrek.blogApp.post.infrastructure.web",
    "com.yusufziyrek.blogApp.post.infrastructure.mappers"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.post.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.post.infrastructure.persistence")
public class PostModuleConfiguration {
    
    @Bean
    public CreatePostUseCase createPostUseCase(PostRepository postRepository) {
        return new CreatePostUseCaseImpl(postRepository);
    }
    
    @Bean
    public GetPostByIdUseCase getPostByIdUseCase(PostRepository postRepository) {
        return new GetPostByIdUseCaseImpl(postRepository);
    }
    
    @Bean
    public GetAllPostsUseCase getAllPostsUseCase(PostRepository postRepository) {
        return new GetAllPostsUseCaseImpl(postRepository);
    }
    
    @Bean
    public UpdatePostUseCase updatePostUseCase(PostRepository postRepository) {
        return new UpdatePostUseCaseImpl(postRepository);
    }
    
    @Bean
    public GetPostsByUserIdUseCase getPostsByUserIdUseCase(PostRepository postRepository) {
        return new GetPostsByUserIdUseCaseImpl(postRepository);
    }
}
