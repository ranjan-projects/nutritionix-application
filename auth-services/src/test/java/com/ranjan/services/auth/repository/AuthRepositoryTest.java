package com.ranjan.services.auth.repository;

import com.ranjan.services.auth.entity.AuthEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;

    private AuthEntity authEntity;

    @BeforeEach
    void setUp() {
        // Create a sample user for testing
        authEntity = AuthEntity.builder()
                .email("test@example.com")
                .password("password")
                .userId(1L)
                .build();

        // Save the user entity to the in-memory database
        authRepository.save(authEntity);
    }

    // Test for finding user by email and password
    @Test
    public void testFindByEmailAndPassword_Success() {
        AuthEntity foundUser = authRepository.findByEmailAndPassword("test@example.com", "password");

        assertEquals(authEntity.getEmail(), foundUser.getEmail());
        assertEquals(authEntity.getPassword(), foundUser.getPassword());
    }

    // Test for not finding user with incorrect email or password
    @Test
    public void testFindByEmailAndPassword_Failure() {
        AuthEntity foundUser = authRepository.findByEmailAndPassword("wrong@example.com", "password");

        assertNull(foundUser);
    }
}
