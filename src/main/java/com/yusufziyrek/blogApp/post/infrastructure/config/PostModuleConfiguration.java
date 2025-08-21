package com.yusufziyrek.blogApp.post.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.yusufziyrek.blogApp.post.application.ports.PostRepository;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.JpaPostRepository;
import com.yusufziyrek.blogApp.post.infrastructure.persistence.PostRepositoryImpl;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.post.application.usecases",
    "com.yusufziyrek.blogApp.post.infrastructure.web"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.post.domain")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.post.infrastructure.persistence")
public class PostModuleConfiguration {
    
    @Bean
    public PostRepository postRepository(JpaPostRepository jpaPostRepository) {
        return new PostRepositoryImpl(jpaPostRepository);
    }
}
