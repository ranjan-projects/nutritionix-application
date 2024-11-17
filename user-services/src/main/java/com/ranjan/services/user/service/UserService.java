package com.ranjan.services.user.service;

import com.ranjan.services.user.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> getAllUsers();

    UserEntity getUserById(long id);

    UserEntity addUser(UserEntity user);

    UserEntity updateUser(UserEntity user);

    UserEntity deleteUser(long id);

}
