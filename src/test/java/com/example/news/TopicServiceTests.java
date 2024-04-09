package com.example.news;

import com.example.news.entity.Topic;
import com.example.news.repository.TopicRepository;
import com.example.news.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceTests {

    @Mock
    private TopicRepository topicRepository;

    private TopicService topicService;

    @BeforeEach
    void setUp() {
        topicRepository = mock(TopicRepository.class);
        topicService = new TopicService(topicRepository);
    }

    @Test
    void testTopicServiceConstructor() {
        // Проверяем, что объект topicService не является null после инициализации
        assertNotNull(topicService);
    }

    @Test
    void testGetAllTopics() {
        // Создаем список тем для теста
        List<Topic> topics = Arrays.asList(
                new Topic(), // Используем конструктор без аргументов
                new Topic(),
                new Topic()
        );

        // Устанавливаем ожидаемое поведение для заглушки topicRepository.findAll()
        when(topicRepository.findAll()).thenReturn(topics);

        // Вызываем метод, который тестируем
        List<Topic> result = topicService.getAllTopics();

        // Проверяем, что результат соответствует ожидаемому списку тем
        assertEquals(topics, result);
    }

    @Test
    void testGetTopicById() {
        // Создаем заглушку для темы
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setCategory("Technology");
        topic.setPopularity(80);

        // Устанавливаем ожидаемое поведение для заглушки репозитория (находим тему)
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        // Вызываем метод, который тестируем
        Topic result = topicService.getTopicById(1L);

        // Проверяем, что метод вернул тему с заданным id
        assertEquals(topic, result);

        // Устанавливаем ожидаемое поведение для заглушки репозитория (темы не существует)
        when(topicRepository.findById(2L)).thenReturn(Optional.empty());

        // Вызываем метод для несуществующего id
        Topic nonExistingResult = topicService.getTopicById(2L);

        // Проверяем, что метод вернул null для несуществующего id
        assertNull(nonExistingResult);
    }

    @Test
    void testDeleteTopic() {
        // Вызываем метод, который тестируем
        topicService.deleteTopic(1L);

        // Проверяем, что метод deleteById() репозитория был вызван один раз с ожидаемым аргументом
        verify(topicRepository, times(1)).deleteById(1L);
    }

    @Test
    public void createTopicTest() {
        Topic topic = new Topic();
        topic.setCategory("Science");
        topic.setPopularity(50);

        when(topicRepository.existsByCategory(anyString())).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        Topic createdTopic = topicService.createTopic(topic);

        assertNotNull(createdTopic);
        assertEquals("Science", createdTopic.getCategory());
        assertEquals(50, createdTopic.getPopularity());

        verify(topicRepository, times(1)).existsByCategory(anyString());
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    public void createTopicWithExistingCategoryTest() {
        Topic topic = new Topic();
        topic.setCategory("Science");

        when(topicRepository.existsByCategory(anyString())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            topicService.createTopic(topic);
        });

        verify(topicRepository, times(1)).existsByCategory(anyString());
        verify(topicRepository, times(0)).save(any(Topic.class));
    }

    @Test
    public void createTopicWithInvalidPopularityTest() {
        Topic topic = new Topic();
        topic.setCategory("Science");
        topic.setPopularity(150);

        when(topicRepository.existsByCategory(anyString())).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        Topic createdTopic = topicService.createTopic(topic);

        assertNotNull(createdTopic);
        assertEquals("Science", createdTopic.getCategory());
        assertEquals(100, createdTopic.getPopularity());

        verify(topicRepository, times(1)).existsByCategory(anyString());
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    public void updateTopicTest() {
        Topic existingTopic = new Topic();
        existingTopic.setId(1L);
        existingTopic.setCategory("Science");
        existingTopic.setPopularity(50);

        Topic newTopic = new Topic();
        newTopic.setCategory("Technology");
        newTopic.setPopularity(80);

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(existingTopic));
        when(topicRepository.save(any(Topic.class))).thenReturn(newTopic);

        Topic updatedTopic = topicService.updateTopic(1L, newTopic);

        assertNotNull(updatedTopic);
        assertEquals("Technology", updatedTopic.getCategory());
        assertEquals(80, updatedTopic.getPopularity());

        verify(topicRepository, times(1)).findById(anyLong());
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    public void updateTopicWithInvalidPopularityTest() {
        Topic existingTopic = new Topic();
        existingTopic.setId(1L);
        existingTopic.setCategory("Science");
        existingTopic.setPopularity(50);

        Topic newTopic = new Topic();
        newTopic.setCategory("Technology");
        newTopic.setPopularity(150);

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(existingTopic));
        when(topicRepository.save(any(Topic.class))).thenAnswer(i -> i.getArguments()[0]);

        Topic updatedTopic = topicService.updateTopic(1L, newTopic);

        assertNotNull(updatedTopic);
        assertEquals("Technology", updatedTopic.getCategory());
        assertEquals(100, updatedTopic.getPopularity());

        verify(topicRepository, times(1)).findById(anyLong());
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    public void updateNonExistentTopicTest() {
        Topic newTopic = new Topic();
        newTopic.setCategory("Technology");
        newTopic.setPopularity(80);

        when(topicRepository.findById(anyLong())).thenReturn(Optional.empty());

        Topic updatedTopic = topicService.updateTopic(1L, newTopic);

        assertNull(updatedTopic);

        verify(topicRepository, times(1)).findById(anyLong());
        verify(topicRepository, times(0)).save(any(Topic.class));
    }

}
