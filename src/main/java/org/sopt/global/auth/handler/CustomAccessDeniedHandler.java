package org.sopt.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.dto.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/* 권한 부족 시 (403 Forbidden) 처리 핸들러 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(ErrorCode.FORBIDDEN.getStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());

        ApiResponse<Void> errorResponse = ApiResponse.of(ErrorCode.FORBIDDEN);
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);

        log.debug("❌ 권한 부족: {}", accessDeniedException.getMessage());
    }
}