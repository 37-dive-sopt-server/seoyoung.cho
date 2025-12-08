package org.sopt.domain.article.service;

import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;
import org.sopt.domain.article.dto.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ArticleResponse create(Long memberId, ArticleCreateRequest request);
    ArticleDetailResponse findById(Long articleId);
    ArticleListResponse findAll();
    ArticleListResponse search(SearchType title, String memberName);

    // 페이지네이션 지원
    Page<Article> findAll(Pageable pageable);
    Page<Article> search(SearchType type, String keyword, Pageable pageable);
}
