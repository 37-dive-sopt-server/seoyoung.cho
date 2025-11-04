package org.sopt.article.service;

import org.sopt.article.domain.Article;
import org.sopt.article.dto.ArticleCreateRequest;

import java.util.List;

public interface ArticleService {
    Article create(ArticleCreateRequest request);
    Article findById(Long articleId);
    List<Article> findAll();
}
