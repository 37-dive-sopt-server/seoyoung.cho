package org.sopt.article.controller;

import jakarta.validation.Valid;
import org.sopt.article.domain.Article;
import org.sopt.article.domain.SearchType;
import org.sopt.article.dto.ArticleCreateRequest;
import org.sopt.article.dto.ArticleListResponse;
import org.sopt.article.dto.ArticleResponse;
import org.sopt.article.service.ArticleService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @Valid @RequestBody ArticleCreateRequest request) {

        Article newArticle = articleService.create(request);
        ArticleResponse response = ArticleResponse.from(newArticle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleResponse>> findArticleById(
            @PathVariable Long articleId) {

        Article article = articleService.findById(articleId);
        ArticleResponse response = ArticleResponse.from(article);

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
