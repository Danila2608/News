package com.example.news;

import com.example.news.cache.NewsCache;
import com.example.news.entity.News;
import com.example.news.entity.Topic;
import com.example.news.exception.NewsAlreadyExistsException;
import com.example.news.repository.NewsRepository;
import com.example.news.service.NewsService;
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
        newsService = new NewsService(newsRepository, newsCache);
    }

    @Test
    void testGetAllNewsFromCache() {
        // Создаем заглушку для списка новостей
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

        // Устанавливаем ожидаемое поведение для заглушки кеша
        when(newsCache.get(anyLong())).thenReturn(cachedNews);

        // Вызываем метод, который тестируем
        List<News> result = newsService.getAllNews();

        // Проверяем, что метод вернул ожидаемый список новостей
        assertEquals(cachedNews, result);

        // Проверяем, что метод findAll() репозитория не был вызван
        verify(newsRepository, never()).findAll();
    }

    @Test
    void testGetAllNewsFromDatabase() {
        // Создаем заглушку для списка новостей из базы данных
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

        // Устанавливаем ожидаемое поведение для заглушки кеша (возвращаем null, чтобы эмулировать отсутствие кешированных данных)
        when(newsCache.get(anyLong())).thenReturn(null);

        // Устанавливаем ожидаемое поведение для заглушки репозитория (возвращаем список новостей из базы данных)
        when(newsRepository.findAll()).thenReturn(databaseNews);

        // Вызываем метод, который тестируем
        List<News> result = newsService.getAllNews();

        // Проверяем, что метод вернул ожидаемый список новостей
        assertEquals(databaseNews, result);

        // Проверяем, что метод findAll() репозитория был вызван один раз
        verify(newsRepository, times(1)).findAll();

        // Проверяем, что метод put() кеша был вызван один раз с ожидаемыми аргументами
        verify(newsCache, times(1)).put(anyLong(), eq(databaseNews));
    }

    @Test
    void testGetNewsById() {
        // Создаем заглушку для новости
        News news = new News();
        news.setId(1L);
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        // Устанавливаем ожидаемое поведение для заглушки репозитория (возвращаем новость с определенным id)
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));

        // Вызываем метод, который тестируем
        News result = newsService.getNewsById(1L);

        // Проверяем, что метод вернул ожидаемую новость
        assertEquals(news, result);
    }

    @Test
    void testDeleteNews() {
        // Вызываем метод, который тестируем
        newsService.deleteNews(1L);

        // Проверяем, что метод deleteById() репозитория был вызван один раз с ожидаемым аргументом
        verify(newsRepository, times(1)).deleteById(1L);

        // Проверяем, что метод clearCache() кеша был вызван один раз
        verify(newsCache, times(1)).clearCache();
    }

    @Test
    void testCreateNews_WhenNewsDoesNotExist() {
        // Создаем новость для теста
        News news = new News();
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        // Устанавливаем ожидаемое поведение для заглушки репозитория (новость существует)
        when(newsRepository.findByTitle("Title")).thenReturn(Optional.of(new News()));

        // Вызываем метод, который тестируем
        assertThrows(NewsAlreadyExistsException.class, () -> newsService.createNews(news));

        // Проверяем, что метод findByTitle() был вызван один раз с ожидаемым аргументом
        verify(newsRepository, times(1)).findByTitle("Title");

        // Проверяем, что метод save() репозитория не был вызван, так как новость уже существует
        verify(newsRepository, never()).save(any(News.class));

        // Проверяем, что метод clearCache() кеша не был вызван, так как новость уже существует
        verify(newsCache, never()).clearCache();
    }

    @Test
    void testCreateNews_WhenNewsExists() {
        // Создаем новость для теста
        News news = new News();
        news.setTitle("Title");
        news.setAuthor("Author");
        news.setDescription("Description");

        // Устанавливаем ожидаемое поведение для заглушки репозитория (новость уже существует)
        when(newsRepository.findByTitle("Title")).thenReturn(Optional.of(news));

        // Вызываем метод, который тестируем и ожидаем исключение
        try {
            newsService.createNews(news);
        } catch (NewsAlreadyExistsException e) {
            // Проверяем, что было выброшено исключение NewsAlreadyExistsException
            assertEquals("News with this title already exists.", e.getMessage());
        }

        // Проверяем, что метод save() репозитория не был вызван
        verify(newsRepository, never()).save(news);

        // Проверяем, что метод clearCache() кеша не был вызван
        verify(newsCache, never()).clearCache();
    }

    @Test
    void testUpdateNews_WhenNewsExists() {
        // Создаем заглушку для существующей новости
        News existingNews = new News();
        existingNews.setId(1L);
        existingNews.setTitle("Existing Title");
        existingNews.setAuthor("Existing Author");
        existingNews.setDescription("Existing Description");

        // Создаем новую версию новости для обновления
        News updatedNews = new News();
        updatedNews.setId(1L); // Устанавливаем тот же ID
        updatedNews.setTitle("Updated Title");
        updatedNews.setAuthor("Updated Author");
        updatedNews.setDescription("Updated Description");

        // Устанавливаем ожидаемое поведение для заглушки репозитория (новость существует)
        when(newsRepository.findById(1L)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(existingNews)).thenReturn(updatedNews); // Возвращаем обновленные данные

        // Вызываем метод, который тестируем
        News result = newsService.updateNews(1L, updatedNews);

        // Проверяем, что метод вернул обновленную новость
        assertEquals(updatedNews, result);

        // Проверяем, что метод findById() репозитория был вызван один раз с ожидаемым аргументом
        verify(newsRepository, times(1)).findById(1L);

        // Проверяем, что метод save() репозитория был вызван один раз с ожидаемой новостью
        verify(newsRepository, times(1)).save(existingNews);

        // Проверяем, что метод clearCache() кеша был вызван один раз
        verify(newsCache, times(1)).clearCache();
    }


    @Test
    void testUpdateNews_WhenNewsDoesNotExist() {
        // Устанавливаем ожидаемое поведение для заглушки репозитория (новость не существует)
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызываем метод, который тестируем
        News result = newsService.updateNews(1L, new News());

        // Проверяем, что метод вернул null, так как новость не существует
        assertNull(result);

        // Проверяем, что метод findById() репозитория был вызван один раз с ожидаемым аргументом
        verify(newsRepository, times(1)).findById(1L);

        // Проверяем, что метод save() репозитория не был вызван, так как новость не существует
        verify(newsRepository, never()).save(any(News.class));

        // Проверяем, что метод clearCache() кеша не был вызван, так как новость не существует
        verify(newsCache, never()).clearCache();
    }

    @Test
    void testGetUsefulNewsByTopic() {
        // Создаем объект темы
        Topic topic = new Topic();
        // Здесь можно установить нужные свойства для объекта темы, если это необходимо

        // Создаем список новостей для данной темы
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Author 1", "Title 1", "Description 1", "Url 1", "UrlToImage 1", LocalDateTime.now(), "Content 1", topic));
        newsList.add(new News("Author 2", "Title 2", "Description 2", "Url 2", "UrlToImage 2", LocalDateTime.now(), "Content 2", topic));

        // Устанавливаем ожидаемое поведение для заглушки репозитория (список новостей для данной темы)
        when(newsRepository.findNewsByTopicId(1L)).thenReturn(newsList);

        // Вызываем метод, который тестируем
        List<News> result = newsService.getUsefulNewsByTopic(1L);

        // Проверяем, что метод вернул список новостей
        assertNotNull(result);
        assertEquals(2, result.size()); // Проверяем, что список содержит две новости

        // Проверяем, что метод findNewsByTopicId() репозитория был вызван один раз с ожидаемым аргументом
        verify(newsRepository, times(1)).findNewsByTopicId(1L);
    }

    @Test
    void testCreateOrUpdateNews() {
        // Создаем список новостей для обновления и создания
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Author 1", "Title 1", "Description 1", "Url 1", "UrlToImage 1", LocalDateTime.now(), "Content 1", null)); // Новая новость
        newsList.add(new News("Author 2", "Title 2", "Description 2", "Url 2", "UrlToImage 2", LocalDateTime.now(), "Content 2", null)); // Новая новость
        newsList.add(new News("Author 3", "Existing Title", "New Description", "Url 3", "UrlToImage 3", LocalDateTime.now(), "Content 3", null)); // Существующая новость

        // Создаем заглушку для метода findByTitle
        when(newsRepository.findByTitle("Title 1")).thenReturn(Optional.empty());
        when(newsRepository.findByTitle("Title 2")).thenReturn(Optional.empty());
        when(newsRepository.findByTitle("Existing Title")).thenReturn(Optional.of(new News())); // Предполагая, что новость с существующим заголовком уже существует в базе данных

        // Устанавливаем ожидаемое поведение для метода save
        when(newsRepository.save(any(News.class))).then(AdditionalAnswers.returnsFirstArg());

        // Вызываем метод, который тестируем
        List<News> result = newsService.createOrUpdateNews(newsList);

        // Проверяем, что метод вернул список новостей
        assertNotNull(result);
        assertEquals(3, result.size()); // Проверяем, что список содержит три новости

        // Проверяем, что метод findByTitle() был вызван три раза с ожидаемыми аргументами
        verify(newsRepository, times(1)).findByTitle("Title 1");
        verify(newsRepository, times(1)).findByTitle("Title 2");
        verify(newsRepository, times(1)).findByTitle("Existing Title");

        // Проверяем, что метод save() был вызван три раза
        verify(newsRepository, times(3)).save(any(News.class));

        // Проверяем, что метод clearCache() был вызван один раз
        verify(newsCache, times(1)).clearCache();
    }

}