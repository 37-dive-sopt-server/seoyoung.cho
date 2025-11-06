package org.sopt.article.service.validator;

import org.sopt.article.dto.ArticleCreateRequest;
import org.sopt.article.exception.DuplicateArticleTitleException;
import org.sopt.article.repository.ArticleRepository;
import org.springframework.stereotype.Component;

@Component
public class ArticleValidator {

    private final ArticleRepository articleRepository;

    public ArticleValidator(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void validateNewArticle(ArticleCreateRequest request) {
        validateDuplicateTitle(request.title());
    }

    private void validateDuplicateTitle(String title) {
        if (articleRepository.existsByTitle(title)) {
            throw new DuplicateArticleTitleException("이미 존재하는 게시글 제목입니다.");
        }
    }
}
