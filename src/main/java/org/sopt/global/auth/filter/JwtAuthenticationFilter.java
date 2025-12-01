package org.sopt.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.auth.service.JwtService;
import org.sopt.global.exception.ExpiredTokenException;
import org.sopt.global.exception.InvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String[] EXCLUDED_PATH_PREFIXES = {
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-config",
            "/swagger-resources"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // Swagger 경로만 필터 건너뜀
        for (String prefix : EXCLUDED_PATH_PREFIXES) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 토큰이 없거나 Bearer 타입이 아니면 검증 로직을 건너뛰고 다음 필터로 넘김
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtService.extractTokenFromHeader(request.getHeader("Authorization"));

            if (token != null) {
                Long memberId = jwtService.verifyAndGetMemberId(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        Collections.emptyList()  // authorities (권한)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("✅ JWT 인증 성공 - memberId: {}", memberId);
            }

        } catch (ExpiredTokenException | InvalidTokenException e) {
            // JWT 검증 실패 시 request에 예외 정보 저장
            // CustomAuthenticationEntryPoint에서 처리
            request.setAttribute("exception", e);
            log.debug("❌ JWT 검증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}