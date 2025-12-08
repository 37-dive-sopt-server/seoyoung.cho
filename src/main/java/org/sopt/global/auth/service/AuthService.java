package org.sopt.global.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.member.domain.Gender;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.domain.Provider;
import org.sopt.domain.member.dto.MemberResponse;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.auth.dto.GoogleTokenResponse;
import org.sopt.global.auth.dto.GoogleUserInfoResponse;
import org.sopt.global.auth.dto.TokenResponse;
import org.sopt.global.exception.EntityNotFoundException;
import org.sopt.global.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final GoogleOAuthService googleOAuthService;
    private final RefreshTokenService refreshTokenService;

    // 소셜 로그인 기본값
    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final Gender DEFAULT_GENDER = Gender.OTHER;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;

    /* 이메일과 비밀번호로 로그인 */
    public TokenResponse login(String email, String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));


        if (member.isSocialMember()) {
            throw new UnauthorizedException("소셜 로그인 회원입니다. " + member.getProvider() + " 로그인을 이용해주세요.");
        }

        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return generateTokens(member);
    }

    /* 구글 소셜 로그인 */
    @Transactional
    public TokenResponse loginWithGoogle(String authorizationCode) {
        log.info("구글 소셜 로그인 시작");

        String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);

        GoogleTokenResponse tokenResponse = googleOAuthService.getAccessToken(decodedCode);

        GoogleUserInfoResponse userInfo = googleOAuthService.getUserInfo(
                tokenResponse.accessToken()
        );

        Member member = memberRepository.findByEmail(userInfo.email())
                .orElseGet(() -> createSocialMember(
                        userInfo.name(),
                        userInfo.email(),
                        userInfo.sub(),
                        Provider.GOOGLE
                ));

        // 기존 회원인데 다른 Provider인 경우 예외
        if (member.getProvider() != Provider.GOOGLE) {
            throw new UnauthorizedException("이미 " + member.getProvider() + " 계정으로 가입된 이메일입니다.");
        }

        log.info("구글 소셜 로그인 성공 - email: {}", member.getEmail());

        return generateTokens(member);
    }

    /* 소셜 회원 생성 */
    private Member createSocialMember(String name, String email, String providerId, Provider provider) {
        log.info("🆕 {} 신규 회원 생성", provider.name());
        log.info("📧 Email: {}", email);
        log.info("👤 Name: {}", name);
        log.info("🆔 Provider ID: {}", providerId);

        Member newMember = Member.createSocialMember(
                name,
                DEFAULT_BIRTHDATE,
                email,
                DEFAULT_GENDER,
                provider,
                providerId
        );

        Member saved = memberRepository.save(newMember);
        log.info("✅ 회원 저장 완료 - ID: {}, Provider: {}, ProviderId: {}",
                saved.getId(), saved.getProvider(), saved.getProviderId());

        return saved;
    }

    public MemberResponse getMemberById(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("로그인되어 있지 않습니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }

    @Transactional
    public TokenResponse refreshAccessToken(String refreshToken) {
        log.info("🔄 토큰 갱신 시작");

        Long memberId = refreshTokenService.validateAndGetMemberId(refreshToken);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return generateTokens(member);
    }

    @Transactional
    public void logout(Long memberId) {
        refreshTokenService.deleteByMemberId(memberId);
        log.info("로그아웃 완료 - memberId: {}", memberId);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return MemberResponse.from(member);
    }

    private TokenResponse generateTokens(Member member) {
        log.info("🎫 토큰 발급 시작 - memberId: {}", member.getId());

        String accessToken = jwtService.generateAccessToken(member.getId(), member.getEmail());
        String refreshToken = jwtService.generateRefreshToken(member.getId());

        long expiresInSeconds = refreshTokenExpireTime / 1000;
        refreshTokenService.saveOrUpdate(member.getId(), refreshToken, expiresInSeconds);

        log.info("토큰 발급 완료 - memberId: {}", member.getId());

        return TokenResponse.of(accessToken, refreshToken);
    }
}
