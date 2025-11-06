package org.sopt.article.service;

import org.sopt.article.service.validator.ArticleValidator;
import org.sopt.global.exception.EntityNotFoundException;
import org.sopt.article.domain.Article;
import org.sopt.article.dto.ArticleCreateRequest;
import org.sopt.article.exception.DuplicateArticleTitleException;
import org.sopt.article.repository.ArticleRepository;
import org.sopt.member.domain.Member;
import org.sopt.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleValidator articleValidator;

    public ArticleServiceImpl(ArticleRepository articleRepository, MemberRepository memberRepository,  ArticleValidator articleValidator) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.articleValidator = articleValidator;
    }

    @Override
    @Transactional
    public Article create(ArticleCreateRequest request) {

        articleValidator.validateNewArticle(request);

        Member member = memberRepository.findById(request.userId())
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
}
