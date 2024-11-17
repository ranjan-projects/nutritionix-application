package com.ranjan.services.user.repository;

import com.ranjan.services.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setName("John Doe");
    }

    @Test
    public void testSaveUser() {
        UserEntity savedUser = userRepository.save(userEntity);

        assertNotNull(savedUser.getUserId());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    public void testFindByEmail() {
        userRepository.save(userEntity);

        UserEntity foundUser = userRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindByEmail_UserNotFound() {
        UserEntity foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    public void testDeleteUser() {
        UserEntity savedUser = userRepository.save(userEntity);

        userRepository.deleteById(savedUser.getUserId());

        UserEntity foundUser = userRepository.findById(savedUser.getUserId()).orElse(null);
        assertNull(foundUser);
    }
}
