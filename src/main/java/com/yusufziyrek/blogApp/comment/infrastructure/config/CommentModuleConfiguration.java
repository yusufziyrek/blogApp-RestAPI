package com.yusufziyrek.blogApp.comment.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.application.usecases.CreateCommentUseCase;
import com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl.CreateCommentUseCaseImpl;
import com.yusufziyrek.blogApp.comment.application.usecases.DeleteCommentUseCase;
import com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl.DeleteCommentUseCaseImpl;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentByIdUseCase;
import com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl.GetCommentByIdUseCaseImpl;
import com.yusufziyrek.blogApp.comment.application.usecases.GetCommentsForPostUseCase;
import com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl.GetCommentsForPostUseCaseImpl;
import com.yusufziyrek.blogApp.comment.application.usecases.UpdateCommentUseCase;
import com.yusufziyrek.blogApp.comment.infrastructure.usecases.impl.UpdateCommentUseCaseImpl;
import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;

/**
 * Configuration for CommentDomain Module
 * Clean Architecture - Infrastructure Layer
 */
@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.comment.infrastructure.persistence",
    "com.yusufziyrek.blogApp.comment.infrastructure.web",
    "com.yusufziyrek.blogApp.comment.infrastructure.mappers",
    "com.yusufziyrek.blogApp.comment.infrastructure.usecases"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.comment.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.comment.infrastructure.persistence")
public class CommentModuleConfiguration {
    
    @Bean
    public CreateCommentUseCase createCommentUseCase(CommentRepository commentRepository, 
                                                     PostRepository postRepository,
                                                     UserRepository userRepository) {
        return new CreateCommentUseCaseImpl(commentRepository, postRepository, userRepository);
    }
    
    @Bean
    public GetCommentByIdUseCase getCommentByIdUseCase(CommentRepository commentRepository) {
        return new GetCommentByIdUseCaseImpl(commentRepository);
    }
    
    @Bean
    public GetCommentsForPostUseCase getCommentsForPostUseCase(CommentRepository commentRepository) {
        return new GetCommentsForPostUseCaseImpl(commentRepository);
    }
    
    @Bean
    public UpdateCommentUseCase updateCommentUseCase(CommentRepository commentRepository) {
        return new UpdateCommentUseCaseImpl(commentRepository);
    }
    
    @Bean
    public DeleteCommentUseCase deleteCommentUseCase(CommentRepository commentRepository,
                                                     PostRepository postRepository) {
        return new DeleteCommentUseCaseImpl(commentRepository, postRepository);
    }
}
