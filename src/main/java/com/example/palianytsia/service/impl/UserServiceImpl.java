package com.example.palianytsia.service.impl;

import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.UserService;

public class UserServiceImpl {
    UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository=userRepository;
    }
}
