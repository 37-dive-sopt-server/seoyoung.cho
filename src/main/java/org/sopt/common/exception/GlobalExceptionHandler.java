package org.sopt.common.exception;

import org.sopt.dto.ApiResponse;
import org.sopt.exception.DataStorageException;
import org.sopt.exception.DuplicateMemberException;
import org.sopt.exception.EntityNotFoundException;
import org.sopt.exception.MemberAgeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request (비즈니스 로직 예외)
    @ExceptionHandler({
            MemberAgeException.class,
            DuplicateMemberException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    // 404 Not Found (데이터 조회 실패 예외)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    // 500 Internal Server Error (파일 I/O, 기타 모든 예외)
    @ExceptionHandler({DataStorageException.class, Exception.class})
    public ResponseEntity<ApiResponse<?>> handleInternalException(Exception e) {
        System.err.println("INTERNAL_SERVER_ERROR: " + e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."));
    }
}
