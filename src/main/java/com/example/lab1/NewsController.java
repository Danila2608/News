package com.example.lab1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class NewsController {

    private static final String API_KEY = "5070f49a6d454c0cbf82c2640dfc8b04";

    @GetMapping("/news")
    public ResponseEntity<Object> getNews(@RequestParam(value = "category", defaultValue = "general") String category) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=" + API_KEY;
        try {
            return ResponseEntity.ok(restTemplate.getForObject(url, Object.class));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category. Please use one of the following categories: business, entertainment, general, health, science, sports, technology.");
        }
    }

}
