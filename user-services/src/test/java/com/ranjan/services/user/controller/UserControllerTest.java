package com.ranjan.services.user.controller;

import com.ranjan.services.user.entity.UserEntity;
import com.ranjan.services.user.exception.CodedException;
import com.ranjan.services.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setName("John Doe");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<UserEntity> users = Arrays.asList(userEntity);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("test@example.com")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_UserExists() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userEntity);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new CodedException(HttpStatus.NOT_FOUND, "NOT_FOUND", "User not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testAddUser() throws Exception {
        when(userService.addUser(any(UserEntity.class))).thenReturn(userEntity);

        String userJson = "{\"email\":\"test@example.com\", \"firstName\":\"John\", \"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).addUser(any(UserEntity.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(any(UserEntity.class))).thenReturn(userEntity);

        String userJson = "{\"userId\":1, \"email\":\"test@example.com\", \"firstName\":\"John\", \"lastName\":\"Doe\"}";

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).updateUser(any(UserEntity.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(userEntity);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        when(userService.deleteUser(1L)).thenThrow(new CodedException(HttpStatus.NOT_FOUND, "NOT_FOUND", "User not found"));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    public void testHandleCodedException() throws Exception {
        CodedException codedException = new CodedException(HttpStatus.NOT_FOUND, "NOT_FOUND", "User not found");

        when(userService.getUserById(1L)).thenThrow(codedException);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found")));

        verify(userService, times(1)).getUserById(1L);
    }
}
