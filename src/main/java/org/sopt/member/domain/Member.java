package org.sopt.member.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.article.domain.Article;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members", indexes = {
        @Index(name = "idx_member_email", columnList = "email", unique = true)
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = true)
    private String providerId; // 소셜 로그인 고유 ID

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    @Builder
    private Member(Long id, String name, String password, LocalDate birthdate,
                   String email, Gender gender, Provider provider, String providerId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.birthdate = birthdate;
        this.email = email;
        this.gender = gender;
        this.provider = provider != null ? provider : Provider.LOCAL;
        this.providerId = providerId;
    }

    public int getAge() {
        return Period.between(this.birthdate, LocalDate.now()).getYears();
    }

    public boolean isLocalMember() {
        return this.provider == Provider.LOCAL;
    }

    public boolean isSocialMember() {
        return this.provider != Provider.LOCAL;
    }

    // 일반 회원가입
    public static Member createLocalMember(String name, String password, LocalDate birthdate,
                                           String email, Gender gender) {
        return Member.builder()
                .name(name)
                .password(password)
                .birthdate(birthdate)
                .email(email)
                .gender(gender)
                .provider(Provider.LOCAL)
                .build();
    }

    // 소셜 로그인
    public static Member createSocialMember(String name, LocalDate birthdate, String email,
                                            Gender gender, Provider provider, String providerId) {
        return Member.builder()
                .name(name)
                .birthdate(birthdate)
                .email(email)
                .gender(gender)
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
