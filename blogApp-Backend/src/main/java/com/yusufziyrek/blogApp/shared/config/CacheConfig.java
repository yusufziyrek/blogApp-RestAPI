package com.yusufziyrek.blogApp.shared.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)    
                .maximumSize(1000)                          
                .recordStats();                            
    }

    @Bean
    public Caffeine<Object, Object> shortLivedCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(500)
                .recordStats();
    }

    @Bean
    public Caffeine<Object, Object> longLivedCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(2000)
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeineConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineConfig);
        
        // Cache isimlerini tanımla
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "userDetails", "allUsers", "allPosts", "postDetails", "userPostTitles", "userPosts",
            "commentsForPost", "commentsForUser", "commentDetails", "likesForPost", "likesForComment"
        ));
        
        return cacheManager;
    }
}