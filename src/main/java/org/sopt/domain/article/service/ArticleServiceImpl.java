package org.sopt.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.service.validator.ArticleValidator;
import org.sopt.global.auth.service.AuthService;
import org.sopt.global.exception.EntityNotFoundException;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.sopt.global.constants.CacheConstants.*;

@Slf4j
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
    @CacheEvict(value = ARTICLES_LIST, allEntries = true)
    public Article create(Long memberId, ArticleCreateRequest request) {

        articleValidator.validateNewArticle(request);

        Member member = memberRepository.findById(memberId)
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
    @Cacheable(value = ARTICLE_DETAIL, key = "#articleId")
    public Article findById(Long articleId) {
        log.info("게시글 조회 - articleId: {} (Cache Miss, DB 조회)", articleId);

        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다."));
    }

    @Override
    @Cacheable(value = ARTICLES_LIST)
    public List<Article> findAll() {
        log.info("게시글 목록 조회 (Cache Miss, DB 조회)");

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
