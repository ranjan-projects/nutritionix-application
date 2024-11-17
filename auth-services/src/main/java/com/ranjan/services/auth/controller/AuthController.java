package com.ranjan.services.auth.controller;

import com.ranjan.services.auth.entity.AuthEntity;
import com.ranjan.services.auth.entity.TokenEntity;
import com.ranjan.services.auth.exception.CodedException;
import com.ranjan.services.auth.model.AuthResponse;
import com.ranjan.services.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Services", description = "Operations related to users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    @Operation(summary = "Authenticate user",
            description = "Authenticate user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised user"),
                    @ApiResponse(responseCode = "500", description = "Something's gone wrong")
            }
    )
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthEntity authEntity) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(authEntity));
    }

    @GetMapping("/token/validate")
    @Operation(summary = "Validate user",
            description = "Validate user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Valid user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Something's gone wrong")
            }
    )
    public ResponseEntity<AuthResponse> validateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) {
        AuthResponse response = authService.validateJwt(authorisation);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/logout")
    @Operation(summary = "Logout user",
            description = "Logout and invalidate token for the user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorised user"),
                    @ApiResponse(responseCode = "500", description = "Something's gone wrong")
            }
    )
    public ResponseEntity<AuthResponse> logoutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) {
        AuthResponse authResponse = authService.invalidateJwt(TokenEntity.builder().token(authorisation).build());

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @ExceptionHandler(CodedException.class)
    public ResponseEntity<CodedException> handleCodedException(CodedException ex) {
        return new ResponseEntity<>(ex, ex.getResponseCode());
    }
}
