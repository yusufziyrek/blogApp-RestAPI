package com.yusufziyrek.blogApp.comment.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.comment.application.ports.CommentRepository;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.JpaCommentRepository;
import com.yusufziyrek.blogApp.comment.infrastructure.persistence.CommentRepositoryImpl;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.comment.application.usecases",
    "com.yusufziyrek.blogApp.comment.infrastructure.web"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.comment.domain")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.comment.infrastructure.persistence")
public class CommentModuleConfiguration {
    
    @Bean
    public CommentRepository commentRepository(JpaCommentRepository jpaCommentRepository) {
        return new CommentRepositoryImpl(jpaCommentRepository);
    }
}
