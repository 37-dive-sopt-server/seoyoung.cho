package org.sopt.common.exception;

import org.sopt.dto.ApiResponse;
import org.sopt.exception.DataStorageException;
import org.sopt.exception.DuplicateMemberException;
import org.sopt.exception.EntityNotFoundException;
import org.sopt.exception.MemberAgeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (비즈니스 로직 예외)
    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MemberAgeException.class,
            DuplicateMemberException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })

    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception e) {
        String message;
        if (e instanceof MethodArgumentTypeMismatchException) {
            message = "유저 ID 형식이 올바르지 않습니다.";
        } else if (e instanceof IllegalArgumentException) {
            message = "성별은 MALE 또는 FEMALE 또는 OTHER 여야합니다.";
        } else if (e instanceof HttpMessageNotReadableException) {
            message = "필드값이 누락되었습니다.";
        } else {
            message = e.getMessage(); // MemberAgeException
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    // 404 Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    // 405 Method Not Allowed (
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
