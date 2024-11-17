package com.ranjan.services.auth.service;

import com.ranjan.services.auth.entity.AuthEntity;
import com.ranjan.services.auth.entity.TokenEntity;
import com.ranjan.services.auth.exception.CodedException;
import com.ranjan.services.auth.model.AuthResponse;
import com.ranjan.services.auth.repository.AuthRepository;
import com.ranjan.services.auth.repository.TokenRepository;
import com.ranjan.services.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ranjan.services.auth.exception.ExceptionEnum.INVALID_TOKEN;
import static com.ranjan.services.auth.exception.ExceptionEnum.UNAUTHORISED_USER;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse authenticateUser(AuthEntity authEntity) {
        AuthEntity auth = authRepository.findByEmailAndPassword(authEntity.getEmail(), authEntity.getPassword());

        if (auth == null) {
            throw new CodedException(UNAUTHORISED_USER.getResponseCode(), UNAUTHORISED_USER.getReasonCode(),
                    UNAUTHORISED_USER.getMessage());
        }

        String token = jwtUtil.createToken(buildClaims(authEntity), authEntity.getEmail());

        tokenRepository.save(TokenEntity.builder()
                .token(token)
                .userId(authEntity.getEmail())
                .logicallyDeleted(false)
                .lastUpdatedDateTime(new Date())
                .build());

        return AuthResponse.builder()
                .token(token)
                .message("Login success")
                .build();
    }

    @Override
    public AuthResponse invalidateJwt(TokenEntity token) {
        Boolean validToken = jwtUtil.validateToken(token.getToken());
        TokenEntity existingToken = tokenRepository.findByTokenAndLogicallyDeleted(token.getToken(), false);

        if (existingToken == null || !validToken) {
            throw new CodedException(INVALID_TOKEN.getResponseCode(), INVALID_TOKEN.getReasonCode(), INVALID_TOKEN.getMessage());
        }

        existingToken.setLogicallyDeleted(true);
        tokenRepository.save(existingToken);

        return AuthResponse.builder().message("Successfully logged out").build();
    }

    @Override
    public AuthResponse validateJwt(String token) {
        Boolean validToken = jwtUtil.validateToken(token);

        TokenEntity existingToken = tokenRepository.findByTokenAndLogicallyDeleted(token, false);

        if (existingToken == null || !validToken) {
            throw new CodedException(INVALID_TOKEN.getResponseCode(), INVALID_TOKEN.getReasonCode(), INVALID_TOKEN.getMessage());
        }

        return AuthResponse.builder()
                .message("Token validated successfully")
                .claims(getClaims(token))
                .build();
    }

    private Map<String, String> getClaims(String token) {
        Map<String, String> claims = new HashMap<>();

        Claims allClaims = jwtUtil.extractAllClaims(token);

        claims.put("username", allClaims.getSubject());
        claims.put("userId", String.valueOf(allClaims.get("userId")));
        claims.put("email", String.valueOf(allClaims.get("email")));

        return claims;
    }


    private Map<String, Object> buildClaims(AuthEntity auth) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", auth.getUserId());
        claims.put("email", auth.getEmail());

        return claims;
    }
}
