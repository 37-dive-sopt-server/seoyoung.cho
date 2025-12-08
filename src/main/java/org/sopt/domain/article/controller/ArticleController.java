package org.sopt.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.sopt.domain.article.dto.ArticleCreateRequest;
import org.sopt.domain.article.dto.ArticleDetailResponse;
import org.sopt.domain.article.dto.ArticleListResponse;
import org.sopt.domain.article.dto.ArticleResponse;
import org.sopt.domain.article.service.ArticleService;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Article", description = "아티클 API")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody ArticleCreateRequest request) {

        ArticleResponse response = articleService.create(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> findArticleById(
            @PathVariable Long articleId) {

        // Service가 캐싱된 DTO 바로 반환
        ArticleDetailResponse response = articleService.findById(articleId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<ArticleListResponse>> findAllArticles() {

        ArticleListResponse response = articleService.findAll();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "게시글 검색", description = "제목 또는 작성자 이름으로 게시글을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ArticleListResponse>> searchArticles(
            @RequestParam SearchType type, // 제목 또는 작성자
            @RequestParam String keyword
    ) {
        ArticleListResponse response = articleService.search(type, keyword);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "게시글 목록 조회 (페이지네이션)", description = "게시글 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Article>>> findAllArticlesWithPagination(
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        Page<Article> articles = articleService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.ok(articles));
    }

    @Operation(summary = "게시글 검색 (페이지네이션)", description = "게시글을 검색하고 페이지네이션하여 조회합니다.")
    @GetMapping("/page/search")
    public ResponseEntity<ApiResponse<Page<Article>>> searchArticlesWithPagination(
            @RequestParam SearchType type,
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        Page<Article> articles = articleService.search(type, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.ok(articles));
    }

}
