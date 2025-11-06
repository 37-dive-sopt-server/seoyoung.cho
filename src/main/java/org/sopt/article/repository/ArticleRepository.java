package org.sopt.article.repository;

import org.sopt.article.domain.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    // N+1 해결을 위한 findAll() 오버라이드
    @EntityGraph(attributePaths = {"member"})
    @Override
    List<Article> findAll();
}

