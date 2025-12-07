package org.sopt.domain.article.service;

import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;

import java.util.List;

public interface ArticleService {
    Article create(Long memberId, ArticleCreateRequest request);
    ArticleDetailResponse findById(Long articleId);
    ArticleListResponse findAll();
    List<Article> search(SearchType title, String memberName);
}
