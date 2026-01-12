package org.sopt.domain.article.repository;

import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> search(SearchType type, String keyword);
    Page<Article> search(SearchType type, String keyword, Pageable pageable);
}