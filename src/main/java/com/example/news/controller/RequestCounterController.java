package com.example.news.controller;

import com.example.news.service.RequestCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
public class RequestCounterController {

    private final RequestCounterService requestCounterService;

    @Autowired
    public RequestCounterController(RequestCounterService requestCounterService) {
        this.requestCounterService = requestCounterService;
    }

    @GetMapping("/value")
    public int getRequestCount() {
        return requestCounterService.getRequestCount();
    }
}