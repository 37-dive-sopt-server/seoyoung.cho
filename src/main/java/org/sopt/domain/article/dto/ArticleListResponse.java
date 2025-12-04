package org.sopt.domain.article.dto;

import org.sopt.domain.article.domain.Article;

import java.util.List;

public record ArticleListResponse(
        List<ArticleResponse> articles
) {
    public static ArticleListResponse from(List<Article> articleList) {
        List<ArticleResponse> articleDtos = articleList.stream()
                .map(ArticleResponse::from)
                .toList();
        return new ArticleListResponse(articleDtos);
    }
}
