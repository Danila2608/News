package com.example.news.service;

import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {

    private int requestCount = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            requestCount++;
        }
    }

    public int getRequestCount() {
        synchronized (lock) {
            return requestCount;
        }
    }

    public void resetRequestCount() {
        synchronized (lock) {
            requestCount = 0;
        }
    }
}