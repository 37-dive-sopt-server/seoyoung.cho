package org.sopt.article.repository;

import org.sopt.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContaining(String title);

    List<Article> findByMemberNameContaining(String memberName);
}

