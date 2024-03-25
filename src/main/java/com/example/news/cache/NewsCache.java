package com.example.news.cache;

import com.example.news.entity.News;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class NewsCache {

    private static final int MAX_SIZE = 5;
    private final Map<Long, List<News>> cache = new LinkedHashMap<Long, List<News>>(MAX_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, List<News>> eldest) {
            return size() > MAX_SIZE;
        }
    };

    public synchronized void put(Long key, List<News> value) {
        cache.put(key, value);
    }

    public synchronized List<News> get(Long key) {
        return cache.getOrDefault(key, null);
    }

    public synchronized boolean containsKey(Long key) {
        return cache.containsKey(key);
    }

    public synchronized void remove(Long key) {
        cache.remove(key);
    }

    public synchronized void clearCache() {
        cache.clear();
    }
}