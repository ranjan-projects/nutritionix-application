package com.ranjan.services.auth.repository;

import com.ranjan.services.auth.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    AuthEntity findByEmailAndPassword(String email, String password);
}
