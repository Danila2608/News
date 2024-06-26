package com.example.news.service;

import com.example.news.entity.Topic;
import com.example.news.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    public Topic createTopic(Topic topic) {
        if (topicRepository.existsByCategory(topic.getCategory())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic with this category already exists.");
        }
        int popularity = topic.getPopularity();
        if (popularity < 0) {
            popularity = 0;
        } else if (popularity > 100) {
            popularity = 100;
        }
        topic.setPopularity(popularity);
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, Topic topic) {
        Topic existingTopic = topicRepository.findById(id)
                .orElse(null);

        if (existingTopic != null) {
            String previousCategory = existingTopic.getCategory();

            if (topic.getCategory() != null) {
                existingTopic.setCategory(topic.getCategory());
            }
            if (topic.getPopularity() >= 0 && topic.getPopularity() <= 100) {
                existingTopic.setPopularity(topic.getPopularity());
            } else {
                existingTopic.setPopularity(100);
            }

            if (topic.getCategory() == null) {
                existingTopic.setCategory(previousCategory);
            }

            return topicRepository.save(existingTopic);
        }
        return null;
    }
}