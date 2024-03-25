package com.example.news.repository;

import com.example.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByTitle(String title);

    @Query("SELECT n FROM News n WHERE n.topic.id = :topicId")
    List<News> findNewsByTopicId(@Param("topicId") Long topicId);
}