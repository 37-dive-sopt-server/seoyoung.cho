package org.sopt.domain.article.dto;

import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.Tag;
import org.sopt.domain.comment.dto.CommentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDetailResponse(
        Long id,
        String title,
        String content,
        Tag tag,
        String memberName,
        LocalDateTime createdAt,
        List<CommentResponse> comments,
        int commentCount
) {
    public static ArticleDetailResponse of(Article article, List<CommentResponse> comments) {
        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getTag(),
                article.getMember().getName(),
                article.getCreatedAt(),
                comments,
                comments.size()
        );
    }
}