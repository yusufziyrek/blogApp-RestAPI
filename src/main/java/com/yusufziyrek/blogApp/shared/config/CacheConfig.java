package com.yusufziyrek.blogApp.shared.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.Set;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> defaultCaffeine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500)
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> defaultCaffeine) {
        String[] names = {
            "userDetails", "allUsers",
            "allPosts", "userPosts", "postDetails", "userPostTitles",
            "commentsForPost", "commentsForUser", "commentDetails"
        };

        return new CaffeineCacheManager(names) {
            {
                setAllowNullValues(false);
            }

            @Override
            protected Cache createCaffeineCache(String cacheName) {
                Set<String> longLived = Set.of("postDetails", "userDetails", "commentDetails");
                if (longLived.contains(cacheName)) {
                    return new CaffeineCache(cacheName,
                        Caffeine.newBuilder()
                            .expireAfterWrite(30, TimeUnit.MINUTES)
                            .maximumSize(200)
                            .recordStats()
                            .build()
                    );
                }
                return new CaffeineCache(cacheName, defaultCaffeine.build());
            }
        };
    }

    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                if (params.length > 0 && params[0] instanceof Pageable) {
                    Pageable pg = (Pageable) params[0];
                    String sort = pg.getSort().toString().replace(":", "=");
                    String base = pg.getPageNumber() + "-" + pg.getPageSize() + "-" + sort;
                    if (params.length > 1) {
                        return base + "-" + Arrays.toString(Arrays.copyOfRange(params, 1, params.length));
                    }
                    return base;
                }
                return SimpleKeyGenerator.generateKey(params);
            }
        };
    }
}