package com.ranjan.services.user.service;

import com.ranjan.services.user.entity.UserEntity;
import com.ranjan.services.user.exception.CodedException;
import com.ranjan.services.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ranjan.services.user.exception.ExceptionEnum.USER_ALREADY_EXIST;
import static com.ranjan.services.user.exception.ExceptionEnum.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setName("John Deo");
    }

    @Test
    public void testGetAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        users.add(userEntity);

        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CodedException exception = assertThrows(CodedException.class, () -> userService.getUserById(1L));

        assertEquals(USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddUser_UserDoesNotExist() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = userService.addUser(userEntity);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail(userEntity.getEmail());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(userEntity);

        CodedException exception = assertThrows(CodedException.class, () -> userService.addUser(userEntity));

        assertEquals(USER_ALREADY_EXIST.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findByEmail(userEntity.getEmail());
        verify(userRepository, never()).save(userEntity);
    }

    @Test
    public void testUpdateUser_UserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity result = userService.updateUser(userEntity);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userEntity.getUserId());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CodedException exception = assertThrows(CodedException.class, () -> userService.updateUser(userEntity));

        assertEquals(USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(userEntity.getUserId());
        verify(userRepository, never()).save(userEntity);
    }

    @Test
    public void testDeleteUser_UserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.deleteUser(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CodedException exception = assertThrows(CodedException.class, () -> userService.deleteUser(1L));

        assertEquals(USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}

