package com.ranjan.services.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_entity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity {

    @Id
    private Long userId;

    private String email;

    private String password;


}
