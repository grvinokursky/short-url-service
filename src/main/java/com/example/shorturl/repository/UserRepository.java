package com.example.shorturl.repository;

import com.example.shorturl.model.UserModel;

import java.util.Optional;

public interface UserRepository {
    void create(String username) throws Exception;
    Optional<UserModel> get(String username);
}