package com.yusufziyrek.blogApp.post.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.CreatePostUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.DeletePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.DeletePostUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetAllPostsUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostByIdUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostsByUserIdUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.GetPostsByUserIdUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCase;
import com.yusufziyrek.blogApp.post.application.usecases.UpdatePostUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.services.PostApplicationService;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.LikePostUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.UnlikeUseCase;
import com.yusufziyrek.blogApp.like.application.usecases.GetPostLikesUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.post.infrastructure.mappers.PostMapper;

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
    
    @Bean
    public DeletePostUseCase deletePostUseCase(PostRepository postRepository) {
        return new DeletePostUseCaseImpl(postRepository);
    }
    
    @Bean
    public PostApplicationService postApplicationService(
            CreatePostUseCase createPostUseCase,
            DeletePostUseCase deletePostUseCase,
            GetAllPostsUseCase getAllPostsUseCase,
            GetPostByIdUseCase getPostByIdUseCase,
            GetPostsByUserIdUseCase getPostsByUserIdUseCase,
            UpdatePostUseCase updatePostUseCase,
            CreateCommentUseCase createCommentUseCase,
            GetCommentsForPostUseCase getCommentsForPostUseCase,
            LikePostUseCase likePostUseCase,
            UnlikeUseCase unlikeUseCase,
            GetPostLikesUseCase getPostLikesUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            PostMapper postMapper) {
        return new PostApplicationService(
                createPostUseCase,
                deletePostUseCase,
                getAllPostsUseCase,
                getPostByIdUseCase,
                getPostsByUserIdUseCase,
                updatePostUseCase,
                createCommentUseCase,
                getCommentsForPostUseCase,
                likePostUseCase,
                unlikeUseCase,
                getPostLikesUseCase,
                getUserByIdUseCase,
                postMapper
        );
    }
}
