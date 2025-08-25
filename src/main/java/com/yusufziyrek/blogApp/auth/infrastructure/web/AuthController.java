package com.yusufziyrek.blogApp.auth.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yusufziyrek.blogApp.auth.application.usecases.CreateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.RegisterUserUseCase;
import com.yusufziyrek.blogApp.auth.application.usecases.ValidateRefreshTokenUseCase;
import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;
import com.yusufziyrek.blogApp.auth.dto.request.LoginRequest;
import com.yusufziyrek.blogApp.auth.dto.request.RefreshTokenRequest;
import com.yusufziyrek.blogApp.auth.dto.request.RegisterRequest;
import com.yusufziyrek.blogApp.auth.dto.response.AuthResponse;
import com.yusufziyrek.blogApp.shared.dto.ApiResponse;
import com.yusufziyrek.blogApp.shared.security.JwtUtil;
import com.yusufziyrek.blogApp.shared.security.UserPrincipal;
import com.yusufziyrek.blogApp.user.application.ports.UserRepository;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByUsernameOrEmailUseCase;
import com.yusufziyrek.blogApp.user.application.usecases.GetUserByIdUseCase;
import com.yusufziyrek.blogApp.user.domain.UserDomain;
import com.yusufziyrek.blogApp.user.dto.response.UserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication Controller
 * Clean Architecture - Infrastructure Layer (Web)
 * Handles authentication related HTTP requests
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CreateRefreshTokenUseCase createRefreshTokenUseCase;
    private final ValidateRefreshTokenUseCase validateRefreshTokenUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserByUsernameOrEmailUseCase getUserByUsernameOrEmailUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        log.info("Registration attempt for email: {}", registerRequest.getEmail());
        
        try {
            // Kullanıcı kaydı
            UserResponse userResponse = registerUserUseCase.execute(registerRequest);
            
            log.info("User registered successfully with ID: {} and email: {}", 
                    userResponse.getId(), userResponse.getEmail());
            
            // Kayıt olan kullanıcıyı otomatik oturum açtır
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registerRequest.getEmail(),
                    registerRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kullanıcı detaylarını al
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // JWT token üret
            String accessToken = jwtUtil.generateToken(userPrincipal.getEmail(), userPrincipal.getId());

            // Cihaz ve IP bilgisiyle refresh token oluştur
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = getClientIpAddress(request);
            
            // Refresh token oluşturmak için UserDomain'i repo'dan çek
            UserDomain userDomain = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            RefreshTokenDomain refreshTokenDomain = createRefreshTokenUseCase.execute(
                userDomain, 
                deviceInfo != null ? deviceInfo : "Unknown Device", 
                ipAddress
            );

            // Süreyi hesapla (ms -> s)
            Long expiresIn = 3600L; // 1 hour default

            AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshTokenDomain.getToken(),
                expiresIn,
                userResponse
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "User registered and authenticated successfully", authResponse));
                    
        } catch (Exception e) {
            log.error("Registration failed for email: {} - {}", registerRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Login attempt for user: {}", loginRequest.getUsernameOrEmail());

        try {
            // Kullanıcıyı doğrula (authenticate)
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kullanıcı detaylarını al
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            UserResponse userResponse = getUserByUsernameOrEmailUseCase.execute(loginRequest.getUsernameOrEmail());

            // JWT token üret
            String accessToken = jwtUtil.generateToken(userPrincipal.getEmail(), userPrincipal.getId());

            // Cihaz ve IP bilgisiyle refresh token oluştur
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = getClientIpAddress(request);
            
            // Refresh token oluşturmak için UserDomain'i repodan al
            UserDomain userDomain = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            RefreshTokenDomain refreshTokenDomain = createRefreshTokenUseCase.execute(
                userDomain, 
                deviceInfo != null ? deviceInfo : "Unknown Device", 
                ipAddress
            );

            // Süre hesaplaması (ms -> s)
            Long expiresIn = 3600L; // 1 hour default

            AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshTokenDomain.getToken(),
                expiresIn,
                userResponse
            );

            log.info("User logged in successfully: {}", loginRequest.getUsernameOrEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));

        } catch (Exception e) {
            log.error("Login failed for user: {} - {}", loginRequest.getUsernameOrEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Invalid username/email or password", null));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        log.info("Token refresh attempt");

        try {
            // Refresh token'ı doğrula
            RefreshTokenDomain refreshTokenDomain = validateRefreshTokenUseCase.execute(refreshRequest.getRefreshToken());

            // ID ile kullanıcı bilgilerini al
            UserResponse userResponse = getUserByIdUseCase.execute(refreshTokenDomain.getUserId());

            // Yeni erişim token'ı oluştur
            String newAccessToken = jwtUtil.generateToken(userResponse.getEmail(), refreshTokenDomain.getUserId());

            Long expiresIn = 3600L; // 1 hour default

            AuthResponse authResponse = new AuthResponse(
                newAccessToken,
                refreshRequest.getRefreshToken(), // Keep the same refresh token
                expiresIn,
                userResponse
            );

            log.info("Token refreshed successfully for user ID: {}", refreshTokenDomain.getUserId());

            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", authResponse));

        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Invalid or expired refresh token", null));
        }
    }

    /**
     * Helper method to extract client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
