package com.example.simplebank.services;

import org.springframework.stereotype.Service;

import com.example.simplebank.models.User;
import com.example.simplebank.repos.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUser(int userId){
        return userRepository.getUserById(userId);
    }

    public User createUser(String name, String email, String password){
        return userRepository.createUser(name, email, password);
    }

    public User authenticate(int userId, String password){
        return userRepository.verifyPassword(userId, password) ? userRepository.getUserById(userId) : null;
    }
}
