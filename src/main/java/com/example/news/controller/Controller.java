package com.example.news.controller;

import com.example.news.entity.News;
import com.example.news.service.NService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class Controller {

    private final NService newsService;

    public Controller(NService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/newsByCategory")
    public ResponseEntity<Object> getNews(@RequestParam(value = "category", defaultValue = "general") String category) {
        return ResponseEntity.ok(newsService.getNews(category));
    }

    @PostMapping("/news/save")
    public ResponseEntity<Object> saveNews(@RequestBody News news) {
        News savedNews = newsService.saveNews(news);
        return ResponseEntity.ok(savedNews);
    }
}