package org.sopt.domain.article.repository;

import static org.sopt.domain.article.domain.QArticle.*;
import static org.sopt.domain.member.domain.QMember.*;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.article.domain.Article;
import org.sopt.domain.article.domain.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

    @Override
    public Page<Article> search(SearchType type, String keyword, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Article> content = switch (type) {
            case TITLE -> queryFactory
                    .selectFrom(article)
                    .leftJoin(article.member, member).fetchJoin()
                    .where(article.title.contains(keyword))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            case MEMBER -> queryFactory
                    .selectFrom(article)
                    .leftJoin(article.member, member).fetchJoin()
                    .where(member.name.contains(keyword))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        };

        // 카운트 쿼리
        JPAQuery<Long> countQuery = switch (type) {
            case TITLE -> queryFactory
                    .select(article.count())
                    .from(article)
                    .where(article.title.contains(keyword));

            case MEMBER -> queryFactory
                    .select(article.count())
                    .from(article)
                    .leftJoin(article.member, member)
                    .where(member.name.contains(keyword));
        };

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
