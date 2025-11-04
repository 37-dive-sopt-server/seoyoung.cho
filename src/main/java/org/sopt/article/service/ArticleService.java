package org.sopt.article.service;

import org.sopt.article.domain.Article;
import org.sopt.article.dto.ArticleCreateRequest;

public interface ArticleService {
    Article create(ArticleCreateRequest request);
    Article findById(Long articleId);
}
