package org.sopt.global.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.auth.domain.RefreshToken;
import org.sopt.global.auth.repository.RefreshTokenRepository;
import org.sopt.global.exception.InvalidTokenException;
import org.sopt.global.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public void saveOrUpdate(Long memberId, String token, long expiresInSeconds) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);

        refreshTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        // 기존 토큰 있으면 갱신
                        existingToken -> {
                            existingToken.updateToken(token, expiresAt);
                            log.info("✅ Refresh Token 갱신 - memberId: {}", memberId);
                        },
                        // 없으면 새로 생성
                        () -> {
                            RefreshToken newToken = RefreshToken.builder()
                                    .memberId(memberId)
                                    .token(token)
                                    .expiresAt(expiresAt)
                                    .build();
                            refreshTokenRepository.save(newToken);
                            log.info("Refresh Token 저장 - memberId: {}", memberId);
                        }
                );
    }

    @Transactional(readOnly = true)
    public Long validateAndGetMemberId(String token) {

        Long memberId = jwtService.verifyAndGetMemberId(token);

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 Refresh Token입니다."));

        if (refreshToken.isExpired()) {
            throw new InvalidTokenException("만료된 Refresh Token입니다.");
        }

        if (!refreshToken.getMemberId().equals(memberId)) {
            throw new InvalidTokenException("토큰 정보가 일치하지 않습니다.");
        }

        return memberId;
    }


    @Transactional
    public void deleteByMemberId(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
        log.info("Refresh Token 삭제 - memberId: {}", memberId);
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        log.info("Refresh Token 삭제 - token: {}...", token.substring(0, 20));
    }
}