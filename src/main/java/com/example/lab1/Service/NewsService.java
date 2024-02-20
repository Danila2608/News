package com.example.lab1.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    @Value("${newsapi.key}")
    private String API_KEY;

    public Object getNews(String category) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + API_KEY;
        return restTemplate.getForObject(url, Object.class);
    }
}