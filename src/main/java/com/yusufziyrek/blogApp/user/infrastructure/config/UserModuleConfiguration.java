package com.yusufziyrek.blogApp.user.infrastructure.config;

import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.JpaUserRepository;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.UserRepositoryImpl;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.user.application.usecases",
    "com.yusufziyrek.blogApp.user.infrastructure.web",
    "com.yusufziyrek.blogApp.user.infrastructure.security"
})
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.user.infrastructure.persistence")
@EntityScan(basePackages = "com.yusufziyrek.blogApp.user.domain")
public class UserModuleConfiguration {
    
    @Bean
    public UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new UserRepositoryImpl(jpaUserRepository);
    }
}
