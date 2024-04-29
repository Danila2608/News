package com.example.news;

import com.example.news.cache.NewsCache;
import com.example.news.entity.News;
import com.example.news.entity.Topic;
import com.example.news.exception.NewsAlreadyExistsException;
import com.example.news.repository.NewsRepository;
import com.example.news.service.NewsService;
import com.example.news.service.RequestCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsCache newsCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        RequestCounterService requestCounterService = new RequestCounterService(); // создаем экземпляр RequestCounterService
        newsService = new NewsService(newsRepository, newsCache, requestCounterService);
    }

    @Test
    void testGetAllNewsFromCache() {
        List<News> cachedNews = new ArrayList<>();
        News news1 = new News();
        news1.setTitle("Title 1");
        news1.setAuthor("Author 1");
        news1.setDescription("Description 1");
        cachedNews.add(news1);

        News news2 = new News();
        news2.setTitle("Title 2");
        news2.setAuthor("Author 2");
        news2.setDescription("Description 2");
        cachedNews.add(news2);

        when(newsCache.get(anyLong())).thenReturn(cachedNews);

        List<News> result = newsService.getAllNews();

        assertEquals(cachedNews, result);

        verify(newsRepository, never()).findAll();
    }

    @Test
    void testGetAllNewsFromDatabase() {
        List<News> databaseNews = new ArrayList<>();
        News news1 = new News();
        news1.setTitle("Title 1");
        news1.setAuthor("Author 1");
        news1.setDescription("Description 1");
        databaseNews.add(news1);

        News news2 = new News();
        news2.setTitle("Title 2");
        news2.setAuthor("Author 2");
        news2.setDescription("Description 2");
        databaseNews.add(news2);

        when(newsCache.get(anyLong())).thenReturn(null);

        when(newsRepository.findAll()).thenReturn(databaseNews);

        List<News> result = newsService.getAllNews();

        assertEquals(databaseNews, result);

        verify(newsRepository, times(1)).findAll();

        verify(newsCache, times(1)).put(anyLong(), eq(databaseNews));
    }

    @Test
    void testGetNewsById() {
        News news = new News();
        news.setId(1L);
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        News result = newsService.getNewsById(1L);

        assertEquals(news, result);
    }

    @Test
    void testDeleteNews() {
        newsService.deleteNews(1L);

        verify(newsRepository, times(1)).deleteById(1L);

        verify(newsCache, times(1)).clearCache();
    }

    @Test
    void testCreateNews_WhenNewsDoesNotExist() {
        News news = new News();
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        when(newsRepository.findByTitle("Title")).thenReturn(Optional.of(new News()));

        assertThrows(NewsAlreadyExistsException.class, () -> newsService.createNews(news));

        verify(newsRepository, times(1)).findByTitle("Title");

        verify(newsRepository, never()).save(any(News.class));

        verify(newsCache, never()).clearCache();
    }

    @Test
    void testCreateNews_WhenNewsExists() {
        News news = new News();
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        when(newsRepository.findByTitle("Title")).thenReturn(Optional.of(news));

        try {
            newsService.createNews(news);
        } catch (NewsAlreadyExistsException e) {
            assertEquals("News with this title already exists.", e.getMessage());
        }

        verify(newsRepository, never()).save(news);

        verify(newsCache, never()).clearCache();
    }

    @Test
    void testUpdateNews_WhenNewsExists() {
        News existingNews = new News();
        existingNews.setId(1L);
        existingNews.setTitle("Existing Title");
        existingNews.setAuthor("Existing Author");
        existingNews.setDescription("Existing Description");

        News updatedNews = new News();
        updatedNews.setId(1L);
        updatedNews.setTitle("Updated Title");
        updatedNews.setAuthor("Updated Author");
        updatedNews.setDescription("Updated Description");

        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(existingNews)).thenReturn(updatedNews);

        News result = newsService.updateNews(1L, updatedNews);

        assertEquals(updatedNews, result);

        verify(newsRepository, times(1)).findById(1L);

        verify(newsRepository, times(1)).save(existingNews);

        verify(newsCache, times(1)).clearCache();
    }


    @Test
    void testUpdateNews_WhenNewsDoesNotExist() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        News result = newsService.updateNews(1L, new News());

        assertNull(result);

        verify(newsRepository, times(1)).findById(1L);

        verify(newsRepository, never()).save(any(News.class));

        verify(newsCache, never()).clearCache();
    }

    @Test
    void testGetUsefulNewsByTopic() {
        Topic topic = new Topic();

        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Author 1", "Title 1", "Description 1", "Url 1", "UrlToImage 1", LocalDateTime.now(), "Content 1", topic));
        newsList.add(new News("Author 2", "Title 2", "Description 2", "Url 2", "UrlToImage 2", LocalDateTime.now(), "Content 2", topic));

        when(newsRepository.findNewsByTopicId(1L)).thenReturn(newsList);

        List<News> result = newsService.getUsefulNewsByTopic(1L);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(newsRepository, times(1)).findNewsByTopicId(1L);
    }

    @Test
    void testCreateOrUpdateNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Author 1", "Title 1", "Description 1", "Url 1", "UrlToImage 1", LocalDateTime.now(), "Content 1", null)); // Новая новость
        newsList.add(new News("Author 2", "Title 2", "Description 2", "Url 2", "UrlToImage 2", LocalDateTime.now(), "Content 2", null)); // Новая новость
        newsList.add(new News("Author 3", "Existing Title", "New Description", "Url 3", "UrlToImage 3", LocalDateTime.now(), "Content 3", null)); // Существующая новость

        when(newsRepository.findByTitle("Title 1")).thenReturn(Optional.empty());
        when(newsRepository.findByTitle("Title 2")).thenReturn(Optional.empty());
        when(newsRepository.findByTitle("Existing Title")).thenReturn(Optional.of(new News()));

        when(newsRepository.save(any(News.class))).then(AdditionalAnswers.returnsFirstArg());

        List<News> result = newsService.createOrUpdateNews(newsList);

        assertNotNull(result);
        assertEquals(3, result.size());

        verify(newsRepository, times(1)).findByTitle("Title 1");
        verify(newsRepository, times(1)).findByTitle("Title 2");
        verify(newsRepository, times(1)).findByTitle("Existing Title");

        verify(newsRepository, times(3)).save(any(News.class));

        verify(newsCache, times(1)).clearCache();
    }

}