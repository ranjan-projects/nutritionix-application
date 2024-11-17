package com.ranjan.services.auth.controller;

import com.ranjan.services.auth.entity.AuthEntity;
import com.ranjan.services.auth.entity.TokenEntity;
import com.ranjan.services.auth.exception.CodedException;
import com.ranjan.services.auth.model.AuthResponse;
import com.ranjan.services.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private AuthEntity authEntity;
    private AuthResponse authResponse;
    private TokenEntity tokenEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authEntity = AuthEntity.builder()
                .email("test@example.com")
                .password("password")
                .userId(1L)
                .build();

        authResponse = AuthResponse.builder()
                .token("test-token")
                .message("Login success")
                .build();

        tokenEntity = TokenEntity.builder()
                .token("test-token")
                .userId(authEntity.getEmail())
                .logicallyDeleted(false)
                .build();
    }

    // Test for authenticateUser method
    @Test
    void testAuthenticateUser() {
        when(authService.authenticateUser(authEntity)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.authenticateUser(authEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());

        verify(authService, times(1)).authenticateUser(authEntity);
    }

    // Test for validateUser method
    @Test
    void testValidateUser() {
        String authorizationHeader = "Bearer test-token";

        when(authService.validateJwt(authorizationHeader)).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.validateUser(authorizationHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());

        verify(authService, times(1)).validateJwt(authorizationHeader);
    }

    // Test for logoutUser method
    @Test
    void testLogoutUser() {
        String authorizationHeader = "Bearer test-token";

        when(authService.invalidateJwt(any(TokenEntity.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.logoutUser(authorizationHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());

        verify(authService, times(1)).invalidateJwt(any(TokenEntity.class));
    }

    // Test for handling CodedException
    @Test
    void testHandleCodedException() {
        CodedException codedException = new CodedException(HttpStatus.UNAUTHORIZED, "401", "Unauthorised user");

        ResponseEntity<CodedException> response = authController.handleCodedException(codedException);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(codedException, response.getBody());
    }
}
