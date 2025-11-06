package org.sopt.article.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.sopt.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "articles", indexes = {
        @Index(name = "idx_article_member_id", columnList = "member_id"),
        @Index(name = "idx_article_created_at", columnList = "created_at DESC"),
        @Index(name = "idx_article_title", columnList = "title", unique = true)
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tag tag;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    private Article(Member member, String title, String content, Tag tag) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    public static Article create(Member member, String title, String content, Tag tag) {
        return Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .tag(tag)
                .build();
    }
}
