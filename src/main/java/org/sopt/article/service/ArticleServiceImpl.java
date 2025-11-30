package org.sopt.article.service;

import lombok.RequiredArgsConstructor;
import org.sopt.article.domain.SearchType;
import org.sopt.article.service.validator.ArticleValidator;
import org.sopt.global.auth.service.AuthService;
import org.sopt.global.exception.EntityNotFoundException;
import org.sopt.article.domain.Article;
import org.sopt.article.dto.ArticleCreateRequest;
import org.sopt.article.repository.ArticleRepository;
import org.sopt.member.domain.Member;
import org.sopt.member.dto.MemberResponse;
import org.sopt.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleValidator articleValidator;
    private final AuthService authService;

    @Override
    @Transactional
    public Article create(String authorization, ArticleCreateRequest request) {

        MemberResponse memberResponse = authService.authenticateWithJwt(authorization);        articleValidator.validateNewArticle(request);

        articleValidator.validateNewArticle(request);

        Member member = memberRepository.findById(memberResponse.userId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다."));

        Article newArticle = Article.create(
                member,
                request.title(),
                request.content(),
                request.tag()
        );

        return articleRepository.save(newArticle);
    }

    @Override
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다."));
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public List<Article> search(SearchType type, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>();
        }

        return articleRepository.search(type, keyword);
    }
}
