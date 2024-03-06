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
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, Topic topic) {
        Topic existingTopic = topicRepository.findById(id).orElse(null);
        if (existingTopic != null) {
            existingTopic.setCategory(topic.getCategory());
            existingTopic.setTotalResults(topic.getTotalResults());
            return topicRepository.save(existingTopic);
        }
        return null;
    }
}

