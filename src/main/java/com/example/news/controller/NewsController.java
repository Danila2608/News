package com.example.news.controller;

import com.example.news.entity.News;
import com.example.news.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getAllNews() {
        logger.info("Fetching all news");
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        logger.info("Fetching news by ID: {}", id);
        News news = newsService.getNewsById(id);
        if (news != null) {
            return ResponseEntity.ok(news);
        } else {
            logger.warn("News not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        logger.info("Deleting news with ID: {}", id);
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        logger.info("Creating news: {}", news);
        News createdNews = newsService.createNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        logger.info("Updating news with ID: {}", id);
        News updatedNews = newsService.updateNews(id, news);
        if (updatedNews != null) {
            return ResponseEntity.ok(updatedNews);
        } else {
            logger.warn("News not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/useful")
    public ResponseEntity<List<News>> getUsefulNewsByTopic(@RequestParam("topicId") Long topicId) {
        logger.info("Fetching useful news by topic ID: {}", topicId);
        List<News> usefulNews = newsService.getUsefulNewsByTopic(topicId);
        if (usefulNews != null && !usefulNews.isEmpty()) {
            return ResponseEntity.ok(usefulNews);
        } else {
            logger.warn("No useful news found with topic ID: {}", topicId);
            return ResponseEntity.notFound().build();
        }
    }
}
