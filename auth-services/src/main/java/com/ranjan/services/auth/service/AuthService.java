package com.ranjan.services.auth.service;

import com.ranjan.services.auth.entity.AuthEntity;
import com.ranjan.services.auth.entity.TokenEntity;
import com.ranjan.services.auth.model.AuthResponse;

public interface AuthService {

    AuthResponse authenticateUser(AuthEntity authEntity);

    AuthResponse invalidateJwt(TokenEntity token);

    AuthResponse validateJwt(String token);


}
