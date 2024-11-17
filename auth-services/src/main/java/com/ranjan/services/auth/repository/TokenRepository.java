package com.ranjan.services.auth.repository;

import com.ranjan.services.auth.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByTokenAndLogicallyDeleted(String token, boolean logicallyDeleted);

}
