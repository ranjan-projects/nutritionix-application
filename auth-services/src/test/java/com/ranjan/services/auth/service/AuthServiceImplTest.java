package com.ranjan.services.auth.service;

import com.ranjan.services.auth.entity.AuthEntity;
import com.ranjan.services.auth.entity.TokenEntity;
import com.ranjan.services.auth.exception.CodedException;
import com.ranjan.services.auth.model.AuthResponse;
import com.ranjan.services.auth.repository.AuthRepository;
import com.ranjan.services.auth.repository.TokenRepository;
import com.ranjan.services.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ranjan.services.auth.exception.ExceptionEnum.INVALID_TOKEN;
import static com.ranjan.services.auth.exception.ExceptionEnum.UNAUTHORISED_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthEntity authEntity;
    private TokenEntity tokenEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authEntity = AuthEntity.builder()
                .email("test@example.com")
                .password("password")
                .userId(1L)
                .build();

        tokenEntity = TokenEntity.builder()
                .token("test-token")
                .userId(authEntity.getEmail())
                .logicallyDeleted(false)
                .lastUpdatedDateTime(new Date())
                .build();
    }

    // Test for authenticateUser method
    @Test
    void testAuthenticateUserSuccess() {
        // Mock the repository and JWT token creation
        when(authRepository.findByEmailAndPassword(authEntity.getEmail(), authEntity.getPassword())).thenReturn(authEntity);
        when(jwtUtil.createToken(anyMap(), eq(authEntity.getEmail()))).thenReturn("test-token");

        AuthResponse response = authService.authenticateUser(authEntity);

        assertNotNull(response);
        assertEquals("Login success", response.getMessage());
        assertEquals("test-token", response.getToken());

        verify(authRepository, times(1)).findByEmailAndPassword(authEntity.getEmail(), authEntity.getPassword());
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));
    }

    @Test
    void testAuthenticateUserUnauthorized() {
        // Simulate no user found in the repository
        when(authRepository.findByEmailAndPassword(authEntity.getEmail(), authEntity.getPassword())).thenReturn(null);

        CodedException exception = assertThrows(CodedException.class, () -> {
            authService.authenticateUser(authEntity);
        });

        assertEquals(UNAUTHORISED_USER.getResponseCode(), exception.getResponseCode());
        assertEquals(UNAUTHORISED_USER.getMessage(), exception.getMessage());

        verify(authRepository, times(1)).findByEmailAndPassword(authEntity.getEmail(), authEntity.getPassword());
        verify(tokenRepository, times(0)).save(any(TokenEntity.class));
    }

    // Test for invalidateJwt method
    @Test
    void testInvalidateJwtSuccess() {
        // Mock JWT validation and token repository
        when(jwtUtil.validateToken(tokenEntity.getToken())).thenReturn(true);
        when(tokenRepository.findByTokenAndLogicallyDeleted(tokenEntity.getToken(), false)).thenReturn(tokenEntity);

        AuthResponse response = authService.invalidateJwt(tokenEntity);

        assertNotNull(response);
        assertEquals("Successfully logged out", response.getMessage());

        verify(tokenRepository, times(1)).save(tokenEntity);
    }

    @Test
    void testInvalidateJwtInvalidToken() {
        // Simulate invalid token
        when(jwtUtil.validateToken(tokenEntity.getToken())).thenReturn(false);
        when(tokenRepository.findByTokenAndLogicallyDeleted(tokenEntity.getToken(), false)).thenReturn(null);

        CodedException exception = assertThrows(CodedException.class, () -> {
            authService.invalidateJwt(tokenEntity);
        });

        assertEquals(INVALID_TOKEN.getResponseCode(), exception.getResponseCode());
        assertEquals(INVALID_TOKEN.getMessage(), exception.getMessage());

        verify(tokenRepository, times(0)).save(any(TokenEntity.class));
    }

    @Test
    void testValidateJwtInvalidToken() {
        // Simulate invalid token
        when(jwtUtil.validateToken(tokenEntity.getToken())).thenReturn(false);
        when(tokenRepository.findByTokenAndLogicallyDeleted(tokenEntity.getToken(), false)).thenReturn(null);

        CodedException exception = assertThrows(CodedException.class, () -> {
            authService.validateJwt(tokenEntity.getToken());
        });

        assertEquals(INVALID_TOKEN.getResponseCode(), exception.getResponseCode());
        assertEquals(INVALID_TOKEN.getMessage(), exception.getMessage());

        verify(jwtUtil, times(1)).validateToken(tokenEntity.getToken());
        verify(tokenRepository, times(1)).findByTokenAndLogicallyDeleted(tokenEntity.getToken(), false);
    }

    // Helper method to create mock claims for the token
    private Map<String, Object> getMockClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        claims.put("email", "test@example.com");
        return claims;
    }
}
