package com.yusufziyrek.blogApp.auth.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.auth.infrastructure.web",
    "com.yusufziyrek.blogApp.auth.infrastructure.persistence",
    "com.yusufziyrek.blogApp.auth.infrastructure.usecases.impl",
    "com.yusufziyrek.blogApp.auth.infrastructure.mappers"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.auth.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.auth.infrastructure.persistence")
@EnableAsync
@EnableScheduling
public class AuthModuleConfiguration {
}
