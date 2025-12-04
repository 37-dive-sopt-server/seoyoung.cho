package org.sopt.domain.article.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;
import org.sopt.domain.article.dto.ArticleResponse;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.domain.comment.domain.Comment;
import org.sopt.domain.comment.dto.CommentResponse;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Article", description = "아티클 API")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;

    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody ArticleCreateRequest request) {


        Article newArticle = articleService.create(memberId, request);
        ArticleResponse response = ArticleResponse.from(newArticle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> findArticleById(
            @PathVariable Long articleId) {

        Article article = articleService.findById(articleId);

        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();

        ArticleDetailResponse response = ArticleDetailResponse.of(article, commentResponses);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ArticleListResponse>> findAllArticles() {

        List<Article> articles = articleService.findAll();
        ArticleListResponse response = ArticleListResponse.from(articles);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ArticleListResponse>> searchArticles(
            @RequestParam SearchType type, // 제목 또는 작성자
            @RequestParam String keyword
    ) {
        List<Article> articles = articleService.search(type, keyword);
        ArticleListResponse response = ArticleListResponse.from(articles);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

}
