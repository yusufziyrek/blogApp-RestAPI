package com.yusufziyrek.blogApp.shared.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching  
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Cache isimlerini tanımla
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "userDetails", "allUsers", "allPosts", "postDetails", "userPostTitles", "userPosts",
            "commentsForPost", "commentsForUser", "commentDetails", "likesForPost", "likesForComment"
        ));
        
        return cacheManager;
    }
}