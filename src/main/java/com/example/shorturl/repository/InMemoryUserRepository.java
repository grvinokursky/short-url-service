package com.example.shorturl.repository;

import com.example.shorturl.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<UserModel> storage = new CopyOnWriteArrayList<>();

    @Override
    public void create(String username) throws Exception {
        if (storage.stream().anyMatch(u -> u.getName().equalsIgnoreCase(username))) {
            throw new Exception("Пользователь с указанным логином не существует.");
        }

        UserModel user = new UserModel();
        user.setName(username);
        storage.add(user);
    }

    @Override
    public Optional<UserModel> get(String username) {
        return storage.stream().filter(u -> u.getName().equalsIgnoreCase(username)).findFirst();
    }
}