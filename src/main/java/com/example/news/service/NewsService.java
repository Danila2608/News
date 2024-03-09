package com.example.news.service;

import com.example.news.cache.NewsCache;
import com.example.news.entity.News;
import com.example.news.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;
    private final NewsCache newsCache;

    public NewsService(NewsRepository newsRepository, NewsCache newsCache) {
        this.newsRepository = newsRepository;
        this.newsCache = newsCache;
    }

    public List<News> getAllNews() {
        Long cacheKey = -1L;
        if (newsCache.containsKey(cacheKey)) {
            logger.debug("Getting all news from cache...");
            return newsCache.get(cacheKey);
        } else {
            logger.debug("Fetching all news from database...");
            List<News> allNews = newsRepository.findAll();
            newsCache.put(cacheKey, allNews);
            return allNews;
        }
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
            String previousAuthor = existingNews.getAuthor();
            String previousTitle = existingNews.getTitle();
            String previousDescription = existingNews.getDescription();

            if (news.getAuthor() != null) {
                existingNews.setAuthor(news.getAuthor());
            }
            if (news.getTitle() != null) {
                existingNews.setTitle(news.getTitle());
            }
            if (news.getDescription() != null) {
                existingNews.setDescription(news.getDescription());
            }

            if (news.getAuthor() == null) {
                existingNews.setAuthor(previousAuthor);
            }
            if (news.getTitle() == null) {
                existingNews.setTitle(previousTitle);
            }
            if (news.getDescription() == null) {
                existingNews.setDescription(previousDescription);
            }

            return newsRepository.save(existingNews);
        }
        return null;
    }

    public List<News> getUsefulNewsByTopic(Long topicId) {
        return newsRepository.findNewsByTopicId(topicId);
    }
}
