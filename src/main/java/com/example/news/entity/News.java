package com.example.news.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private LocalDateTime publishedAt;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @JsonBackReference
    private Topic topic;

    public News() {
        // Пустой конструктор
    }

    private News(Builder builder) {
        this.author = builder.author;
        this.title = builder.title;
        this.description = builder.description;
        this.url = builder.url;
        this.urlToImage = builder.urlToImage;
        this.publishedAt = builder.publishedAt;
        this.content = builder.content;
        this.topic = builder.topic;
    }

    public static class Builder {
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private LocalDateTime publishedAt;
        private String content;
        private Topic topic;

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder urlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
            return this;
        }

        public Builder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder topic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public News build() {
            return new News(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) &&
                Objects.equals(author, news.author) &&
                Objects.equals(title, news.title) &&
                Objects.equals(description, news.description) &&
                Objects.equals(url, news.url) &&
                Objects.equals(urlToImage, news.urlToImage) &&
                Objects.equals(publishedAt, news.publishedAt) &&
                Objects.equals(content, news.content) &&
                Objects.equals(topic, news.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, description, url, urlToImage, publishedAt, content, topic);
    }
}