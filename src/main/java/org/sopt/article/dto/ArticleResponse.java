package org.sopt.article.dto;

import org.sopt.article.domain.Article;
import org.sopt.article.domain.Tag;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        Tag tag,
        String memberName,
        LocalDateTime createdAt
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getTag(),
                article.getMember().getName(),
                article.getCreatedAt()
        );
    }
}
