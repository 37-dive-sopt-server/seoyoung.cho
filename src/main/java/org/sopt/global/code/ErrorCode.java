package org.sopt.global.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400 Bad Request
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "요청 본문(JSON)의 구문이 잘못되었습니다."),
    INVALID_USER_ID_FORMAT(HttpStatus.BAD_REQUEST, "유저 ID 형식이 올바르지 않습니다."),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    MEMBER_AGE_INVALID(HttpStatus.BAD_REQUEST, "회원 나이가 유효하지 않습니다."),
    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    DUPLICATE_ARTICLE_TITLE(HttpStatus.BAD_REQUEST, "이미 존재하는 게시글 제목입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 404 Not Found
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "지원하지 않는 URL입니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    DATA_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장 중 오류가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}