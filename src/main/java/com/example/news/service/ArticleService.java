package com.example.news.service;

import com.example.news.entity.Article;
import com.example.news.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article updateArticle(Long id, Article article) {
        Article existingArticle = articleRepository.findById(id).orElse(null);
        if (existingArticle != null) {
            // Обновление данных
            existingArticle.setStatus(article.getStatus());
            existingArticle.setTotalResults(article.getTotalResults());
            // сохранение обновленной сущности
            return articleRepository.save(existingArticle);
        }
        return null; // В случае, если статья не найдена
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.orElse(null);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }
}
