package org.sopt.domain.article.service.validator;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.exception.DuplicateArticleTitleException;
import org.sopt.domain.article.repository.ArticleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleValidator {

    private final ArticleRepository articleRepository;

    public void validateNewArticle(ArticleCreateRequest request) {
        validateDuplicateTitle(request.title());
    }

    private void validateDuplicateTitle(String title) {
        if (articleRepository.existsByTitle(title)) {
            throw new DuplicateArticleTitleException("이미 존재하는 게시글 제목입니다.");
        }
    }
}