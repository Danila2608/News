package com.example.news.service;

import com.example.news.entity.Topic;
import com.example.news.repository.TopicRepository;
import org.springframework.stereotype.Service;

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
        if (topic.getPopularity() < 0 || topic.getPopularity() > 100) {
            topic.setPopularity(Math.min(Math.max(topic.getPopularity(), 0), 100));
        }
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

            if (existingTopic.getPopularity() <= 100) {
                return topicRepository.save(existingTopic);
            } else {
                return null;
            }
        }
        return null;
    }
}