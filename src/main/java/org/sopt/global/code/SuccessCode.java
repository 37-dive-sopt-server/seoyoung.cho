package org.sopt.global.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    // 2xx Success
    OK(HttpStatus.OK, "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "✅ 리소스가 성공적으로 생성되었습니다."),

    // Member
    MEMBER_CREATED(HttpStatus.CREATED, "✅ 회원이 성공적으로 생성되었습니다."),
    MEMBER_UPDATED(HttpStatus.OK, "회원 정보가 수정되었습니다."),
    MEMBER_DELETED(HttpStatus.OK, "회원이 삭제되었습니다."),

    // Article
    ARTICLE_CREATED(HttpStatus.CREATED, "✅ 게시글이 성공적으로 생성되었습니다."),
    ARTICLE_UPDATED(HttpStatus.OK, "게시글이 수정되었습니다."),
    ARTICLE_DELETED(HttpStatus.OK, "게시글이 삭제되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}