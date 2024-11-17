package com.ranjan.services.user.controller;

import com.ranjan.services.user.entity.UserEntity;
import com.ranjan.services.user.exception.CodedException;
import com.ranjan.services.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Services", description = "Operations related to users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user by their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Something's gone wrong")
            }
    )
    public ResponseEntity<UserEntity> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "Add a new user", description = "Add a new user to the system")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity userEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userEntity));
    }

    @PutMapping
    @Operation(summary = "Update an existing user", description = "Update details of an existing user by their ID")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity userEntity) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userEntity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Remove a user from the system by their ID")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }

    @ExceptionHandler(CodedException.class)
    public ResponseEntity<CodedException> handleCodedException(CodedException ex) {
        return new ResponseEntity<>(ex, ex.getResponseCode());
    }
}
