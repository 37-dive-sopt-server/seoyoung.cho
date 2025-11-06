package org.sopt.global.exception;

import org.sopt.article.exception.DuplicateArticleTitleException;
import org.sopt.global.dto.ApiResponse;
import org.sopt.member.exception.DuplicateMemberException;
import org.sopt.member.exception.MemberAgeException;
import org.springframework.http.HttpStatus;
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

    // 400 Bad Request (비즈니스 로직 예외)
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MemberAgeException.class,
            DuplicateMemberException.class,
            DuplicateArticleTitleException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })

    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception e) {
        String message;
        if (e instanceof HttpMessageNotReadableException) { // JSON 파싱 실패
            Throwable cause = e.getCause(); // 원인 확인

            if (cause != null && cause.getCause() instanceof InvalidFormatException) { // enum 파싱 오류
                message = cause.getCause().getMessage();
            } else {
                message = "요청 본문(JSON)의 구문이 잘못되었습니다.";
            }
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "유저 ID 형식이 올바르지 않습니다.";
        } else if (e instanceof IllegalArgumentException) { // Validator가 던진 예외
            message = e.getMessage();
        } else { // 비즈니스 로직 예외
            message = e.getMessage();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFound(NoHandlerFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "지원하지 않는 URL입니다."));
    }

    // 404 Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    // 405 Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED) // 405
                .body(ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(), "잘못된 HTTP method 요청입니다."));
    }

    // 500 Internal Server Error
    @ExceptionHandler({DataStorageException.class, Exception.class})
    public ResponseEntity<ApiResponse<?>> handleInternalException(Exception e) {
        System.err.println("INTERNAL_SERVER_ERROR: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."));
    }
}
