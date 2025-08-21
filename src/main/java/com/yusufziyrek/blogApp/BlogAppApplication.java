package com.yusufziyrek.blogApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import com.yusufziyrek.blogApp.post.infrastructure.config.PostModuleConfiguration;
import com.yusufziyrek.blogApp.comment.infrastructure.config.CommentModuleConfiguration;
import com.yusufziyrek.blogApp.user.infrastructure.config.UserModuleConfiguration;
import com.yusufziyrek.blogApp.like.infrastructure.config.LikeModuleConfiguration;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching
@Slf4j
@Import({
    PostModuleConfiguration.class,
    CommentModuleConfiguration.class,
    UserModuleConfiguration.class,
    LikeModuleConfiguration.class
})
public class BlogAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
		log.info("BlogAppApplication started successfully with Clean Architecture - Domain-based Modular Monolith structure.");
	}

}
