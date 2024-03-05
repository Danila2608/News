package com.example.news.service;

import com.example.news.entity.Article;
import com.example.news.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, Article article) {
        Article existingArticle = articleRepository.findById(id).orElse(null);
        if (existingArticle != null) {
            existingArticle.setStatus(article.getStatus());
            existingArticle.setTotalResults(article.getTotalResults());
            // Обновление остальных полей...
            return articleRepository.save(existingArticle);
        }
        return null;
    }
}

