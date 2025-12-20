package com.yusufziyrek.blogApp.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.yusufziyrek.blogApp.auth.domain.RefreshTokenDomain;

/**
 * Unit tests for RefreshTokenDomain
 * Tests domain business logic for refresh tokens
 */
@DisplayName("RefreshTokenDomain Tests")
class RefreshTokenDomainTest {

    private RefreshTokenDomain refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshTokenDomain(
                1L,
                "valid.refresh.token.value",
                100L,
                LocalDateTime.now().plusDays(7),
                false,
                LocalDateTime.now());
    }

    @Nested
    @DisplayName("Token State Methods")
    class TokenStateMethods {

        @Test
        @DisplayName("Should return not expired for future expiry date")
        void shouldReturnNotExpiredForFutureExpiryDate() {
            // When/Then
            assertThat(refreshToken.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should return expired for past expiry date")
        void shouldReturnExpiredForPastExpiryDate() {
            // Given
            RefreshTokenDomain expiredToken = new RefreshTokenDomain(
                    2L,
                    "expired.token",
                    100L,
                    LocalDateTime.now().minusDays(1),
                    false,
                    LocalDateTime.now().minusDays(8));

            // When/Then
            assertThat(expiredToken.isExpired()).isTrue();
        }

        @Test
        @DisplayName("Should be active when not revoked and not expired")
        void shouldBeActiveWhenNotRevokedAndNotExpired() {
            // When/Then
            assertThat(refreshToken.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should not be active when revoked")
        void shouldNotBeActiveWhenRevoked() {
            // Given
            refreshToken.revoke();

            // When/Then
            assertThat(refreshToken.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should not be active when expired")
        void shouldNotBeActiveWhenExpired() {
            // Given
            RefreshTokenDomain expiredToken = new RefreshTokenDomain(
                    2L,
                    "expired.token",
                    100L,
                    LocalDateTime.now().minusHours(1),
                    false,
                    LocalDateTime.now().minusDays(8));

            // When/Then
            assertThat(expiredToken.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Revocation")
    class Revocation {

        @Test
        @DisplayName("Should revoke token")
        void shouldRevokeToken() {
            // Given
            assertThat(refreshToken.getIsRevoked()).isFalse();

            // When
            refreshToken.revoke();

            // Then
            assertThat(refreshToken.getIsRevoked()).isTrue();
        }
    }

    @Nested
    @DisplayName("Expiry Extension")
    class ExpiryExtension {

        @Test
        @DisplayName("Should extend expiry by specified days")
        void shouldExtendExpiryBySpecifiedDays() {
            // Given
            LocalDateTime originalExpiry = refreshToken.getExpiresAt();

            // When
            refreshToken.extendExpiry(5);

            // Then
            assertThat(refreshToken.getExpiresAt()).isAfter(originalExpiry);
            assertThat(refreshToken.getExpiresAt()).isEqualTo(originalExpiry.plusDays(5));
        }

        @Test
        @DisplayName("Should throw exception when extending revoked token")
        void shouldThrowExceptionWhenExtendingRevokedToken() {
            // Given
            refreshToken.revoke();

            // When/Then
            assertThatThrownBy(() -> refreshToken.extendExpiry(5))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("revoked");
        }
    }

    @Nested
    @DisplayName("Token Creation Validation")
    class TokenCreationValidation {

        @Test
        @DisplayName("Should throw exception for null token string")
        void shouldThrowExceptionForNullTokenString() {
            // When/Then
            assertThatThrownBy(() -> new RefreshTokenDomain(null, 1L, LocalDateTime.now().plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for empty token string")
        void shouldThrowExceptionForEmptyTokenString() {
            // When/Then
            assertThatThrownBy(() -> new RefreshTokenDomain("", 1L, LocalDateTime.now().plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for invalid user ID")
        void shouldThrowExceptionForInvalidUserId() {
            // When/Then
            assertThatThrownBy(
                    () -> new RefreshTokenDomain("valid.token.string", null, LocalDateTime.now().plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for past expiry date")
        void shouldThrowExceptionForPastExpiryDate() {
            // When/Then
            assertThatThrownBy(() -> new RefreshTokenDomain("valid.token.string", 1L, LocalDateTime.now().minusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Getters and Setters")
    class GettersAndSetters {

        @Test
        @DisplayName("Should get and set device info")
        void shouldGetAndSetDeviceInfo() {
            // When
            refreshToken.setDeviceInfo("Chrome on Windows");

            // Then
            assertThat(refreshToken.getDeviceInfo()).isEqualTo("Chrome on Windows");
        }

        @Test
        @DisplayName("Should get and set IP address")
        void shouldGetAndSetIpAddress() {
            // When
            refreshToken.setIpAddress("192.168.1.1");

            // Then
            assertThat(refreshToken.getIpAddress()).isEqualTo("192.168.1.1");
        }
    }
}
