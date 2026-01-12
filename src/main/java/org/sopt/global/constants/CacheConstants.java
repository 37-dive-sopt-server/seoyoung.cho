package org.sopt.global.constants;

/* 캐시 관련 상수 */
public class CacheConstants {
    private CacheConstants() {
        throw new AssertionError("상수 클래스는 인스턴스화할 수 없습니다.");
    }

    // Cache Names
    public static final String ARTICLES_LIST = "articlesList"; // 게시글 목록

    public static final String ARTICLE_DETAIL = "articleDetail"; // 게시글 상세


    // TTL (minutes)
    public static final long DEFAULT_TTL_MINUTES = 5L;

    public static final long ARTICLES_LIST_TTL_MINUTES = 5L;

    public static final long ARTICLE_DETAIL_TTL_MINUTES = 10L;


    // Key Patterns
    public static final String ARTICLES_LIST_KEY = "'sopt:articles:list:v1'";

    public static final String ARTICLE_DETAIL_KEY_PREFIX = "sopt:article:";
    public static final String ARTICLE_DETAIL_KEY_SUFFIX = ":v1";
}
