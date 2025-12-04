package org.sopt.domain.article.repository;

import static org.sopt.domain.article.domain.QArticle.*;
import static org.sopt.domain.member.domain.QMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> search(SearchType type, String keyword) {
        // N+1 해결을 위해 fetchJoin() 사용
        return switch (type) {
            case TITLE -> queryFactory
                    .selectFrom(article)
                    .leftJoin(article.member, member).fetchJoin()
                    .where(article.title.contains(keyword))
                    .fetch();

            case MEMBER -> queryFactory
                    .selectFrom(article)
                    .leftJoin(article.member, member).fetchJoin()
                    .where(member.name.contains(keyword))
                    .fetch();
        };
    }
}
