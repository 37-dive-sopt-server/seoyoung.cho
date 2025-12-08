package org.sopt.global.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.sopt.global.exception.ExpiredTokenException;
import org.sopt.global.exception.InvalidTokenException;
import org.sopt.global.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final long accessTokenExpireSeconds;
    private final long refreshTokenExpireSeconds;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        // 밀리초 → 초
        this.accessTokenExpireSeconds = accessTokenExpireTime / 1000;
        this.refreshTokenExpireSeconds = refreshTokenExpireTime / 1000;
    }

    public String generateAccessToken(Long memberId, String email) {
        return generateToken(memberId, email, accessTokenExpireSeconds);
    }

    public String generateRefreshToken(Long memberId) {
        return generateToken(memberId, null, refreshTokenExpireSeconds);
    }

    // JWT 토큰 생성
    private String generateToken(Long memberId, String email, long expiresInSeconds) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiresInSeconds);

        JWTCreator.Builder builder = JWT.create()
                .withSubject(String.valueOf(memberId))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp));

        // email이 있을 때만 추가
        if (email != null) {
            builder.withClaim("email", email);
        }

        return builder.sign(algorithm);
    }

    public String extractTokenFromHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new UnauthorizedException("Authorization 헤더가 없습니다.");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Bearer 토큰 형식이 아닙니다.");
        }

        return authorization.substring("Bearer ".length()).trim();
    }


    public Long verifyAndGetMemberId(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            String sub = jwt.getSubject();

            try {
                return Long.parseLong(sub);
            } catch (NumberFormatException e) {
                throw new InvalidTokenException("JWT의 회원 정보가 올바르지 않습니다.");
            }
        } catch (TokenExpiredException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

    public String verifyAndGetEmail(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("토큰이 없습니다.");
        }

        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return jwt.getClaim("email").asString();
        } catch (TokenExpiredException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }
}

