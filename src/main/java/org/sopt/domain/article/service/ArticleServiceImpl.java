package org.sopt.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;
import org.sopt.domain.article.dto.ArticleResponse;
import org.sopt.domain.article.repository.ArticleRepository;
import org.sopt.domain.article.service.validator.ArticleValidator;
import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.comment.dto.CommentResponse;
import org.sopt.domain.comment.repository.CommentRepository;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.sopt.global.constants.CacheConstants.ARTICLES_LIST;
import static org.sopt.global.constants.CacheConstants.ARTICLE_DETAIL;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleValidator articleValidator;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    @CacheEvict(value = ARTICLES_LIST, allEntries = true)
    public ArticleResponse create(Long memberId, ArticleCreateRequest request) {

        articleValidator.validateNewArticle(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        Article newArticle = Article.create(member, request.title(), request.content(), request.tag());
        Article savedArticle = articleRepository.save(newArticle);

        return ArticleResponse.from(savedArticle);
    }

    @Override
    @Cacheable(value = ARTICLE_DETAIL, key = "#articleId")
    public ArticleDetailResponse findById(Long articleId) {
        log.info("게시글 조회 - articleId: {} (Cache Miss, DB 조회)", articleId);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(EntityNotFoundException::new);

        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();

        // 댓글 개수 카운트 쿼리로 최적화
        long commentCount = commentRepository.countByArticleId(articleId);

        return ArticleDetailResponse.of(article, commentResponses, commentCount);
    }

    @Override
    @Cacheable(value = ARTICLES_LIST)
    public ArticleListResponse findAll() {
        log.info("게시글 목록 조회 (Cache Miss, DB 조회)");

        List<Article> articles = articleRepository.findAll();

        return ArticleListResponse.from(articles);
    }

    @Override
    public ArticleListResponse search(SearchType type, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return ArticleListResponse.from(new ArrayList<>());
        }

        List<Article> articles = articleRepository.search(type, keyword);

        return ArticleListResponse.from(articles);
    }

    @Override
    public Page<Article> findAll(Pageable pageable) {
        log.info("게시글 목록 조회 (페이지네이션) - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> search(SearchType type, String keyword, Pageable pageable) {
        log.info("게시글 검색 (페이지네이션) - type: {}, keyword: {}, page: {}", type, keyword, pageable.getPageNumber());

        if (keyword == null || keyword.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.search(type, keyword, pageable);
    }
}
