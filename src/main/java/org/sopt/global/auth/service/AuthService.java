package org.sopt.global.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lombok.RequiredArgsConstructor;
import org.sopt.global.exception.UnauthorizedException;
import org.sopt.member.domain.Member;
import org.sopt.member.dto.MemberResponse;
import org.sopt.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public MemberResponse loginWithCredentials(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new UnauthorizedException("이메일과 비밀번호를 모두 입력해주세요.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return MemberResponse.from(member);
    }

    public MemberResponse authenticateWithJwt(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("유효하지 않은 인증 정보입니다.");
        }

        String token = authorization.substring("Bearer ".length()).trim();
        Long memberId = jwtService.verifyAndGetMemberId(token);

        return getMemberById(memberId);
    }

    public MemberResponse getMemberById(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("로그인되어 있지 않습니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }
}
