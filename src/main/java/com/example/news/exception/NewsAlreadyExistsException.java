package com.example.news.exception;

public class NewsAlreadyExistsException extends RuntimeException {
    public NewsAlreadyExistsException(String message) {
        super(message);
    }
}
