package org.sopt.domain.article.repository;

import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> search(SearchType type, String keyword);
}