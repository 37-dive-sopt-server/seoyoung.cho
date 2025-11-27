package org.sopt.global.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.auth.dto.GoogleTokenResponse;
import org.sopt.global.auth.dto.GoogleUserInfoResponse;
import org.sopt.global.exception.UnauthorizedException;
import org.sopt.member.domain.Gender;
import org.sopt.member.domain.Member;
import org.sopt.member.domain.Provider;
import org.sopt.member.dto.MemberResponse;
import org.sopt.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final GoogleOAuthService googleOAuthService;

    // 소셜 로그인 기본값
    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final Gender DEFAULT_GENDER = Gender.OTHER;

    /* 이메일과 비밀번호로 로그인 */
    public MemberResponse loginWithCredentials(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new UnauthorizedException("이메일과 비밀번호를 모두 입력해주세요.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));


        if (member.isSocialMember()) {
            throw new UnauthorizedException("소셜 로그인 회원입니다. " + member.getProvider() + " 로그인을 이용해주세요.");
        }

        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return MemberResponse.from(member);
    }

    /* 구글 소셜 로그인 */
    @Transactional
    public MemberResponse loginWithGoogle(String authorizationCode) {
        log.info("구글 소셜 로그인 시작");

        GoogleTokenResponse tokenResponse = googleOAuthService.getAccessToken(authorizationCode);

        GoogleUserInfoResponse userInfo = googleOAuthService.getUserInfo(tokenResponse.accessToken());

        Member member = memberRepository.findByEmail(userInfo.email())
                .orElseGet(() -> createGoogleMember(userInfo));

        // 기존 회원인데 다른 Provider인 경우 예외
        if (member.getProvider() != Provider.GOOGLE) {
            throw new UnauthorizedException("이미 " + member.getProvider() + " 계정으로 가입된 이메일입니다.");
        }

        log.info("구글 소셜 로그인 성공 - email: {}", member.getEmail());
        return MemberResponse.from(member);
    }

    /* 구글 회원 생성 */
    private Member createGoogleMember(GoogleUserInfoResponse userInfo) {
        log.info("구글 신규 회원 생성 - email: {}", userInfo.email());

        Member newMember = Member.createSocialMember(
                userInfo.name(),
                DEFAULT_BIRTHDATE,
                userInfo.email(),
                DEFAULT_GENDER,
                Provider.GOOGLE,
                userInfo.sub()
        );

        return memberRepository.save(newMember);
    }

    public MemberResponse authenticateWithJwt(String authorization) {
        log.debug("Authorization header: {}", authorization);

        String token = jwtService.extractTokenFromHeader(authorization);
        log.debug("Extracted token: {}", token);

        Long memberId = jwtService.verifyAndGetMemberId(token);
        log.debug("Member ID: {}", memberId);

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
