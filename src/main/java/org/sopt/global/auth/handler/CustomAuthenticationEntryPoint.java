package org.sopt.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.dto.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/* 인증 실패 시 (401 Unauthorized) 처리 핸들러 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // JwtAuthenticationFilter에서 저장한 예외 정보 확인
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, ErrorCode.UNAUTHORIZED);
            return;
        }

        // 예외 타입에 따라 에러 응답 생성
        if (exception instanceof org.sopt.global.exception.ExpiredTokenException) {
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
        } else if (exception instanceof org.sopt.global.exception.InvalidTokenException) {
            setResponse(response, ErrorCode.INVALID_TOKEN);
        } else {
            setResponse(response, ErrorCode.UNAUTHORIZED);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        ApiResponse<Void> errorResponse = ApiResponse.of(errorCode);
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);

        log.debug("❌ 인증 실패 응답: {} - {}", errorCode.getStatusCode(), errorCode.getMessage());
    }
}