package com.example.news.exception;

public class Exception400 extends RuntimeException {
    public Exception400(String message) {
        super("Bad Request: " + message);
    }
}