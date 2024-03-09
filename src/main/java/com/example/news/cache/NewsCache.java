package com.example.news.cache;

import com.example.news.entity.News;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class NewsCache {

    private final int maxSize = 5; // Максимальный общий размер кэша
    private final Map<Long, List<News>> cache = new LinkedHashMap<Long, List<News>>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, List<News>> eldest) {
            return sizeInBytes() > maxSize;
        }
    };

    private int sizeInBytes() {
        // Рассчитываем общий размер кэша в байтах (примерно)
        // Реализация этого метода зависит от ваших требований
        return cache.size() * 1000; // Предположим, что каждый элемент кэша имеет средний размер 1000 байт
    }

    public void put(Long key, List<News> value) {
        cache.put(key, value);
    }

    public List<News> get(Long key) {
        return cache.get(key);
    }

    public boolean containsKey(Long key) {
        return cache.containsKey(key);
    }

    public void remove(Long key) {
        cache.remove(key);
    }
}
