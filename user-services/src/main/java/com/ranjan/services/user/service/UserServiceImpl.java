package com.ranjan.services.user.service;

import com.ranjan.services.user.entity.UserEntity;
import com.ranjan.services.user.exception.CodedException;
import com.ranjan.services.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ranjan.services.user.exception.ExceptionEnum.USER_ALREADY_EXIST;
import static com.ranjan.services.user.exception.ExceptionEnum.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CodedException(USER_NOT_FOUND.getResponseCode(), USER_NOT_FOUND.getReasonCode(), USER_NOT_FOUND.getMessage());
        }

        return user.get();
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        UserEntity existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new CodedException(USER_ALREADY_EXIST.getResponseCode(), USER_ALREADY_EXIST.getReasonCode(), USER_ALREADY_EXIST.getMessage());
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findById(user.getUserId());
        if (existingUser.isEmpty()) {
            throw new CodedException(USER_NOT_FOUND.getResponseCode(), USER_NOT_FOUND.getReasonCode(), USER_NOT_FOUND.getMessage());
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity deleteUser(long userId) {
        Optional<UserEntity> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new CodedException(USER_NOT_FOUND.getResponseCode(), USER_NOT_FOUND.getReasonCode(), USER_NOT_FOUND.getMessage());
        }
        userRepository.deleteById(userId);

        return existingUser.get();
    }
}
