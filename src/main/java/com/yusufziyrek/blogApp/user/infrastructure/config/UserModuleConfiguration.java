package com.yusufziyrek.blogApp.user.infrastructure.config;

import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.*;
import com.yusufziyrek.blogApp.user.application.usecases.impl.*;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.JpaUserRepository;
import com.yusufziyrek.blogApp.user.infrastructure.persistence.UserRepositoryImpl;
import com.yusufziyrek.blogApp.user.infrastructure.security.PasswordEncoderAdapter;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * User modülü yapılandırması - bağımlılıkları birbirine bağlıyorum
 */
@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.user.infrastructure.web",
    "com.yusufziyrek.blogApp.user.infrastructure.persistence"
})
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.user.infrastructure.persistence")
@EntityScan(basePackages = "com.yusufziyrek.blogApp.user.infrastructure.persistence.entity")
public class UserModuleConfiguration {
    
    // Repository adaptörü
    @Bean
    public UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new UserRepositoryImpl(jpaUserRepository);
    }
    
    // Parola encoder adaptörü
    @Bean
    public PasswordEncoder userPasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder) {
        return new PasswordEncoderAdapter(springPasswordEncoder);
    }
    
    // Use case implementasyonları
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordEncoder userPasswordEncoder) {
        return new CreateUserUseCaseImpl(userRepository, userPasswordEncoder);
    }
    
    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserRepository userRepository) {
        return new GetUserByIdUseCaseImpl(userRepository);
    }
    
    @Bean
    public GetAllUsersUseCase getAllUsersUseCase(UserRepository userRepository) {
        return new GetAllUsersUseCaseImpl(userRepository);
    }
    
    @Bean
    public GetUserByEmailUseCase getUserByEmailUseCase(UserRepository userRepository) {
        return new GetUserByEmailUseCaseImpl(userRepository);
    }
    
    @Bean
    public GetUserByUsernameOrEmailUseCase getUserByUsernameOrEmailUseCase(UserRepository userRepository) {
        return new GetUserByUsernameOrEmailUseCaseImpl(userRepository);
    }
    
    @Bean
    public UpdateUserProfileUseCase updateUserProfileUseCase(UserRepository userRepository) {
        return new UpdateUserProfileUseCaseImpl(userRepository);
    }
}
