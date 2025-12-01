package org.sopt.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리 (BusinessException 상속한 모든 예외)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("❌ BusinessException: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.of(errorCode));
    }

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        log.warn("❌ Validation Error: {}", e.getMessage());

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(ErrorCode.INVALID_INPUT_VALUE, message));
    }

    // JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e
    ) {
        log.warn("❌ JSON Parsing Error: {}", e.getMessage());

        Throwable cause = e.getCause();
        if (cause != null && cause.getCause() instanceof InvalidFormatException) {
            String message = cause.getCause().getMessage();
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.of(ErrorCode.INVALID_REQUEST_BODY, message));
        }

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(ErrorCode.INVALID_REQUEST_BODY));
    }

    // 타입 미스매치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e
    ) {
        log.warn("❌ Type Mismatch: {}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(ErrorCode.INVALID_USER_ID_FORMAT));
    }

    // URL 없음 (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(
            NoHandlerFoundException e
    ) {
        log.warn("❌ URL Not Found: {}", e.getRequestURL());

        return ResponseEntity
                .status(ErrorCode.URL_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.URL_NOT_FOUND));
    }

    // HTTP Method 불일치 (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException e
    ) {
        log.warn("❌ Method Not Allowed: {}", e.getMethod());

        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ApiResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    // IllegalArgumentException (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        log.warn("❌ Illegal Argument: {}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(ErrorCode.INVALID_ARGUMENT, e.getMessage()));
    }

    // 그 외 모든 예외 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("❌ Unexpected Error: ", e);

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}