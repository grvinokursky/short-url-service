package com.example.shorturl.model;

import java.util.UUID;

public class UserModel {
    private UUID id = UUID.randomUUID();
    private String name;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}