package com.yusufziyrek.blogApp.like.infrastructure.config;

import com.yusufziyrek.blogApp.like.application.ports.LikeRepository;
import com.yusufziyrek.blogApp.like.infrastructure.persistence.JpaLikeRepository;
import com.yusufziyrek.blogApp.like.infrastructure.persistence.LikeRepositoryImpl;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.like.application.usecases",
    "com.yusufziyrek.blogApp.like.infrastructure.web"
})
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.like.infrastructure.persistence")
@EntityScan(basePackages = "com.yusufziyrek.blogApp.like.domain")
public class LikeModuleConfiguration {
    
    @Bean
    public LikeRepository likeRepository(JpaLikeRepository jpaLikeRepository) {
        return new LikeRepositoryImpl(jpaLikeRepository);
    }
}
