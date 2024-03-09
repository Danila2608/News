package com.example.news.controller;

import com.example.news.entity.News;
import com.example.news.service.NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final NService newsService;

    public Controller(NService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/newsByCategory")
    public ResponseEntity<Object> getNews(@RequestParam(value = "category", defaultValue = "general") String category) {
        logger.info("Received request to get news by category: {}", category);
        return ResponseEntity.ok(newsService.getNews(category));
    }

    @PostMapping("/news/save")
    public ResponseEntity<Object> saveNews(@RequestBody News news) {
        logger.info("Received request to save news: {}", news.getTitle());
        News savedNews = newsService.saveNews(news);
        logger.info("News saved successfully: {}", savedNews.getId());
        return ResponseEntity.ok(savedNews);
    }
}