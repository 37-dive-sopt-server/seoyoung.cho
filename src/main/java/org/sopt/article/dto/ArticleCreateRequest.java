package org.sopt.article.dto;

import org.sopt.article.domain.Tag;

public record ArticleCreateRequest(
        Long userId,
        String title,
        String content,
        Tag tag
) {
}
