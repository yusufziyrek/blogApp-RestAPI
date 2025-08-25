package com.yusufziyrek.blogApp.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.yusufziyrek.blogApp.auth.application.ports.RefreshTokenRepository;
import com.yusufziyrek.blogApp.auth.application.usecases.CleanupExpiredTokensUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.CreateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.RegisterUserUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.RevokeRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.ValidateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.impl.CleanupExpiredTokensUseCaseImpl;
import com.yusufziyrek.blogApp.auth.application.usecases.impl.CreateRefreshTokenUseCaseImpl;
import com.yusufziyrek.blogApp.auth.application.usecases.impl.RegisterUserUseCaseImpl;
import com.yusufziyrek.blogApp.auth.application.usecases.impl.RevokeRefreshTokenUseCaseImpl;
import com.yusufziyrek.blogApp.auth.application.usecases.impl.ValidateRefreshTokenUseCaseImpl;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.user.application.ports.PasswordEncoder;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;

@Configuration
@ComponentScan(basePackages = {
    "com.yusufziyrek.blogApp.auth.infrastructure.web",
    "com.yusufziyrek.blogApp.auth.infrastructure.persistence",
    "com.yusufziyrek.blogApp.auth.infrastructure.mappers"
})
@EntityScan(basePackages = "com.yusufziyrek.blogApp.auth.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.yusufziyrek.blogApp.auth.infrastructure.persistence")
@EnableAsync
@EnableScheduling
public class AuthModuleConfiguration {
    
    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, @Qualifier("userPasswordEncoder") PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCaseImpl(userRepository, passwordEncoder);
    }
    
    @Bean
    public CleanupExpiredTokensUseCase cleanupExpiredTokensUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new CleanupExpiredTokensUseCaseImpl(refreshTokenRepository);
    }
    
    @Bean
    public RevokeRefreshTokenUseCase revokeRefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        return new RevokeRefreshTokenUseCaseImpl(refreshTokenRepository, userRepository);
    }
    
    @Bean
    public CreateRefreshTokenUseCase createRefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        return new CreateRefreshTokenUseCaseImpl(refreshTokenRepository, jwtUtil, 5); // Max 5 tokens per user
    }
    
    @Bean
    public ValidateRefreshTokenUseCase validateRefreshTokenUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new ValidateRefreshTokenUseCaseImpl(refreshTokenRepository);
    }
    
    // Diğer auth use case'ler şimdilik ComponentScan ile çalışacak
    // Gerekirse bean tanımları sonra eklenebilir
}
