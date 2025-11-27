package org.sopt.global.exception;

import org.sopt.article.exception.DuplicateArticleTitleException;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.dto.ApiResponse;
import org.sopt.member.exception.DuplicateMemberException;
import org.sopt.member.exception.MemberAgeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request - JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        if (cause != null && cause.getCause() instanceof InvalidFormatException) {
            // enum 파싱 오류인 경우 원인 메시지 사용
            String message = cause.getCause().getMessage();
            return ResponseEntity
                    .status(ErrorCode.INVALID_REQUEST_BODY.getHttpStatus())
                    .body(ApiResponse.of(ErrorCode.INVALID_REQUEST_BODY, message));
        }

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST_BODY.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INVALID_REQUEST_BODY));
    }

    // 400 Bad Request - 타입 미스매치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_USER_ID_FORMAT.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INVALID_USER_ID_FORMAT));
    }

    // 400 Bad Request - 회원 나이 예외
    @ExceptionHandler(MemberAgeException.class)
    public ResponseEntity<ApiResponse<?>> handleMemberAgeException(MemberAgeException e) {
        return ResponseEntity
                .status(ErrorCode.MEMBER_AGE_INVALID.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.MEMBER_AGE_INVALID, e.getMessage()));
    }

    // 400 Bad Request - 중복 회원
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateMemberException(DuplicateMemberException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATE_MEMBER.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.DUPLICATE_MEMBER, e.getMessage()));
    }

    // 400 Bad Request - 중복 게시글 제목
    @ExceptionHandler(DuplicateArticleTitleException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateArticleTitleException(DuplicateArticleTitleException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATE_ARTICLE_TITLE.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.DUPLICATE_ARTICLE_TITLE, e.getMessage()));
    }

    // 400 Bad Request - IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_ARGUMENT.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INVALID_ARGUMENT, e.getMessage()));
    }

    // 400 Bad Request - Validation 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INVALID_INPUT_VALUE, message));
    }

    // 401 Unauthorized - 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage()));
    }

    // 401 Unauthorized - 유효하지 않은 토큰
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity
                .status(ErrorCode.INVALID_TOKEN.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INVALID_TOKEN, e.getMessage()));
    }

    // 401 Unauthorized - 만료된 토큰
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredTokenException(ExpiredTokenException e) {
        return ResponseEntity
                .status(ErrorCode.EXPIRED_TOKEN.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.EXPIRED_TOKEN, e.getMessage()));
    }

    // 404 Not Found - URL 없음
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFound(NoHandlerFoundException e) {
        return ResponseEntity
                .status(ErrorCode.URL_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.URL_NOT_FOUND));
    }

    // 404 Not Found - 엔티티 없음
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.ENTITY_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.ENTITY_NOT_FOUND, e.getMessage()));
    }

    // 405 Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    // 500 Internal Server Error - 데이터 저장 실패
    @ExceptionHandler(DataStorageException.class)
    public ResponseEntity<ApiResponse<?>> handleDataStorageException(DataStorageException e) {
        System.err.println("DATA_STORAGE_ERROR: " + e.getMessage());

        return ResponseEntity
                .status(ErrorCode.DATA_STORAGE_ERROR.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.DATA_STORAGE_ERROR));
    }

    // 500 Internal Server Error - 기타 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        System.err.println("INTERNAL_SERVER_ERROR: " + e.getMessage());
        e.printStackTrace();

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}