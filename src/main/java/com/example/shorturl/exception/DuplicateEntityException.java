package com.example.shorturl.exception;

public class DuplicateEntityException extends Exception {
    public DuplicateEntityException(String msg) { super(msg); }
}