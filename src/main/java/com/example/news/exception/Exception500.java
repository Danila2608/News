package com.example.news.exception;

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super("Internal Server Error: " + message);
    }
}
