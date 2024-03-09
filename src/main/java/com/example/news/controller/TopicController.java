package com.example.news.controller;

import com.example.news.entity.Topic;
import com.example.news.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> getAllTopics() {
        logger.info("Fetching all topics");
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        logger.info("Fetching topic by ID: {}", id);
        Topic topic = topicService.getTopicById(id);
        if (topic != null) {
            return ResponseEntity.ok(topic);
        } else {
            logger.warn("Topic not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        logger.info("Deleting topic with ID: {}", id);
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        logger.info("Creating topic: {}", topic);
        Topic createdTopic = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topic) {
        logger.info("Updating topic with ID: {}", id);
        Topic updatedTopic = topicService.updateTopic(id, topic);
        if (updatedTopic != null) {
            return ResponseEntity.ok(updatedTopic);
        } else {
            logger.warn("Topic not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
