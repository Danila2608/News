package com.example.news.service;

import com.example.news.entity.News;
import com.example.news.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News news) {
        News existingNews = newsRepository.findById(id).orElse(null);
        if (existingNews != null) {
            existingNews.setAuthor(news.getAuthor());
            existingNews.setTitle(news.getTitle());
            existingNews.setDescription(news.getDescription());
            return newsRepository.save(existingNews);
        }
        return null;
    }
}
