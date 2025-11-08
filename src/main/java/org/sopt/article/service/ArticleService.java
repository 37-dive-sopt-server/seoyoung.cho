package org.sopt.article.service;

import org.sopt.article.domain.Article;
import org.sopt.article.domain.SearchType;
import org.sopt.article.dto.ArticleCreateRequest;

import java.util.List;

public interface ArticleService {
    Article create(ArticleCreateRequest request);
    Article findById(Long articleId);
    List<Article> findAll();
    List<Article> search(SearchType title, String memberName);
}
