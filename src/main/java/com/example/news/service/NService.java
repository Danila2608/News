package com.example.news.service;

import com.example.news.entity.News;
import com.example.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NService {

    private final String apiKey;
    private final NewsRepository newsRepository;

    public NService(@Value("${newsapi.key}") String apiKey, NewsRepository newsRepository) {
        this.apiKey = apiKey;
        this.newsRepository = newsRepository;
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public Object getNews(String category) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + apiKey;
        News news = restTemplate.getForObject(url, News.class);

        saveNews(news);

        return news;
    }
}
