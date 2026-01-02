package com.example.shorturl.service;

import com.example.shorturl.model.UserModel;
import com.example.shorturl.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public void register(String username) throws Exception {
        userRepo.create(username);
    }

    public Optional<UserModel> getByName(String username) {
        return userRepo.get(username);
    }
}