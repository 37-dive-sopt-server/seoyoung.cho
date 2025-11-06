package org.sopt.article.repository;

import org.sopt.article.domain.Article;
import org.sopt.article.domain.SearchType;
import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> search(SearchType type, String keyword);
}