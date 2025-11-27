package org.sopt.global.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Date;

import org.sopt.global.exception.ExpiredTokenException;
import org.sopt.global.exception.InvalidTokenException;
import org.sopt.global.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final long defaultExpiresInSeconds;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expires-in-seconds:3600}") long defaultExpiresInSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.defaultExpiresInSeconds = defaultExpiresInSeconds;
    }

    public String generateToken(Long memberId, String email) {
        return generateToken(memberId, email, defaultExpiresInSeconds);
    }

    public String generateToken(Long memberId, String email, long expiresInSeconds) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiresInSeconds);

        return JWT.create()
                .withSubject(String.valueOf(memberId))
                .withClaim("email", email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
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
            return Long.parseLong(sub);
        } catch (TokenExpiredException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.", e);
        }
    }
}

