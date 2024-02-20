package com.example.lab1.Controller;

import com.example.lab1.Service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public ResponseEntity<Object> getNews(@RequestParam(value = "category", defaultValue = "general") String category) {
        try {
            return ResponseEntity.ok(newsService.getNews(category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category. Please use one of the following categories: business, entertainment, general, health, science, sports, technology.");
        }
    }
}