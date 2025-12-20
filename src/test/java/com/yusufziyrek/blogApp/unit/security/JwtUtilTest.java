package com.yusufziyrek.blogApp.unit.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.yusufziyrek.blogApp.shared.security.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * Unit tests for JwtUtil
 * Tests token generation, validation, and claim extraction
 */
@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Test secret key (Base64 encoded, at least 256 bits for HS256)
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvclVuaXRUZXN0aW5nUHVycG9zZXNPbmx5MTIzNDU2Nzg=";
    private static final long EXPIRATION_TIME = 3600000L; // 1 hour
    private static final long REFRESH_EXPIRATION_TIME = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET, EXPIRATION_TIME, REFRESH_EXPIRATION_TIME);
    }

    @Nested
    @DisplayName("Token Generation Tests")
    class TokenGenerationTests {

        @Test
        @DisplayName("Should generate valid access token with claims")
        void shouldGenerateValidAccessToken() {
            // Given
            String email = "test@example.com";
            Long userId = 1L;
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            // When
            String token = jwtUtil.generateToken(email, userId, authorities);

            // Then
            assertThat(token).isNotNull().isNotEmpty();
            assertThat(jwtUtil.validateToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should include email as subject in token")
        void shouldIncludeEmailAsSubject() {
            // Given
            String email = "user@blog.com";
            Long userId = 42L;
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            // When
            String token = jwtUtil.generateToken(email, userId, authorities);
            String extractedEmail = jwtUtil.extractEmail(token);

            // Then
            assertThat(extractedEmail).isEqualTo(email);
        }

        @Test
        @DisplayName("Should include userId in token claims")
        void shouldIncludeUserIdInClaims() {
            // Given
            String email = "admin@blog.com";
            Long userId = 100L;
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

            // When
            String token = jwtUtil.generateToken(email, userId, authorities);
            Long extractedUserId = jwtUtil.extractUserId(token);

            // Then
            assertThat(extractedUserId).isEqualTo(userId);
        }

        @Test
        @DisplayName("Should include roles in token claims")
        void shouldIncludeRolesInClaims() {
            // Given
            String email = "admin@blog.com";
            Long userId = 1L;
            var authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"));

            // When
            String token = jwtUtil.generateToken(email, userId, authorities);
            Claims claims = jwtUtil.extractAllClaims(token);

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");

            // Then
            assertThat(roles).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
        }

        @Test
        @DisplayName("Should generate refresh token with type claim")
        void shouldGenerateRefreshTokenWithTypeClaim() {
            // Given
            String email = "user@example.com";

            // When
            String refreshToken = jwtUtil.generateRefreshToken(email);

            // Then
            assertThat(refreshToken).isNotNull().isNotEmpty();
            assertThat(jwtUtil.validateRefreshToken(refreshToken)).isTrue();
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should validate correct token")
        void shouldValidateCorrectToken() {
            // Given
            String token = jwtUtil.generateToken("test@test.com", 1L,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // When
            boolean isValid = jwtUtil.validateToken(token);

            // Then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should reject invalid token")
        void shouldRejectInvalidToken() {
            // Given
            String invalidToken = "invalid.token.here";

            // When
            boolean isValid = jwtUtil.validateToken(invalidToken);

            // Then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Should reject tampered token")
        void shouldRejectTamperedToken() {
            // Given
            String token = jwtUtil.generateToken("test@test.com", 1L,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
            String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";

            // When
            boolean isValid = jwtUtil.validateToken(tamperedToken);

            // Then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Should reject expired token")
        void shouldRejectExpiredToken() {
            // Given - Create JwtUtil with very short expiration (1ms)
            JwtUtil shortLivedJwtUtil = new JwtUtil(TEST_SECRET, 1L, 1L);
            String token = shortLivedJwtUtil.generateToken("test@test.com", 1L,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // Wait for token to expire
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // When/Then
            assertThat(shortLivedJwtUtil.validateToken(token)).isFalse();
        }

        @Test
        @DisplayName("Should throw ExpiredJwtException when extracting claims from expired token")
        void shouldThrowExceptionForExpiredTokenClaims() {
            // Given
            JwtUtil shortLivedJwtUtil = new JwtUtil(TEST_SECRET, 1L, 1L);
            String token = shortLivedJwtUtil.generateToken("test@test.com", 1L,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // When/Then
            assertThatThrownBy(() -> shortLivedJwtUtil.extractAllClaims(token))
                    .isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Nested
    @DisplayName("Refresh Token Tests")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should validate valid refresh token")
        void shouldValidateValidRefreshToken() {
            // Given
            String refreshToken = jwtUtil.generateRefreshToken("user@test.com");

            // When
            boolean isValid = jwtUtil.validateRefreshToken(refreshToken);

            // Then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should reject access token as refresh token")
        void shouldRejectAccessTokenAsRefreshToken() {
            // Given - Generate an access token (not a refresh token)
            String accessToken = jwtUtil.generateToken("user@test.com", 1L,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // When
            boolean isValid = jwtUtil.validateRefreshToken(accessToken);

            // Then
            assertThat(isValid).isFalse();
        }

        @Test
        @DisplayName("Should extract email from refresh token")
        void shouldExtractEmailFromRefreshToken() {
            // Given
            String email = "refresh@example.com";
            String refreshToken = jwtUtil.generateRefreshToken(email);

            // When
            String extractedEmail = jwtUtil.extractEmailFromRefreshToken(refreshToken);

            // Then
            assertThat(extractedEmail).isEqualTo(email);
        }
    }
}
