package org.sopt.global.dto;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;

public class ApiResponse<T> {
    private final int code; // HTTP 상태 코드
    private final String message; // 응답 메시지
    private final T data; // 실제 응답 데이터

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getStatusCode(), successCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getStatusCode(), successCode.getMessage(), null);
    }

    // 200 OK
    public static <T> ApiResponse<T> ok(T data) {
        return of(SuccessCode.OK, data);
    }

    // 201 Created
    public static <T> ApiResponse<T> created(T data) {
        return of(SuccessCode.CREATED, data);
    }

    public static <T> ApiResponse<T> of(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatusCode(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> of(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(errorCode.getStatusCode(), customMessage, null);
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}