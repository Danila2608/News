package com.example.news.service;

import com.example.news.cache.NewsCache;
import com.example.news.entity.News;
import com.example.news.exception.LogException;
import com.example.news.exception.NewsAlreadyExistsException;
import com.example.news.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;
    private final NewsCache newsCache;

    public NewsService(NewsRepository newsRepository, NewsCache newsCache) {
        this.newsRepository = newsRepository;
        this.newsCache = newsCache;
    }

    public List<News> createOrUpdateNews(List<News> newsList) {
        // Используем Stream API для обхода списка новостей
        newsList.forEach(news -> {
            // Проверяем, существует ли новость с таким заголовком
            Optional<News> existingNews = newsRepository.findByTitle(news.getTitle());
            existingNews.ifPresentOrElse(
                    // Если новость существует, обновляем ее
                    n -> {
                        n.setAuthor(news.getAuthor());
                        n.setDescription(news.getDescription());
                        n.setUrl(news.getUrl());
                        n.setUrlToImage(news.getUrlToImage());
                        n.setPublishedAt(news.getPublishedAt());
                        n.setContent(news.getContent());
                        newsRepository.save(n);
                    },
                    // Если новости нет, создаем новую
                    () -> newsRepository.save(news)
            );
        });

        // Очищаем кэш после выполнения bulk операции
        newsCache.clearCache();

        // Возвращаем список обновленных или созданных новостей
        return newsList;
    }

    public List<News> getAllNews() {
        Long cacheKey = -1L;
        List<News> cachedNews = newsCache.get(cacheKey);
        if (cachedNews != null) {
            logger.info("Fetching all news from cache...");
            return cachedNews;
        } else {
            logger.info("Fetching all news from database...");
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
        newsCache.clearCache();
    }

    @LogException
    public News createNews(News news) {
        if (newsRepository.findByTitle(news.getTitle()).isPresent()) {
            throw new NewsAlreadyExistsException("News with this title already exists.");
        }

        newsCache.clearCache();

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

            newsCache.clearCache();

            return newsRepository.save(existingNews);

        }
        return null;
    }

    public List<News> getUsefulNewsByTopic(Long topicId) {
        return newsRepository.findNewsByTopicId(topicId);
    }
}
