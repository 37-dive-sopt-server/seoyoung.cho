package org.sopt.domain.article.service;

import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;
import org.sopt.domain.article.dto.ArticleResponse;

public interface ArticleService {
    ArticleResponse create(Long memberId, ArticleCreateRequest request);
    ArticleDetailResponse findById(Long articleId);
    ArticleListResponse findAll();
    ArticleListResponse search(SearchType title, String memberName);
}
