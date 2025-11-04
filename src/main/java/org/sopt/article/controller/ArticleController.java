package org.sopt.article.controller;

import org.sopt.article.domain.Article;
import org.sopt.article.dto.ArticleCreateRequest;
import org.sopt.article.dto.ArticleResponse;
import org.sopt.article.service.ArticleService;
import org.sopt.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(
            @RequestBody ArticleCreateRequest request) {

        Article newArticle = articleService.create(request);
        ArticleResponse response = ArticleResponse.from(newArticle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }
}
