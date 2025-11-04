package org.sopt.article.repository;

import org.sopt.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);

    Optional<Article> findByTitle(String title);
}

