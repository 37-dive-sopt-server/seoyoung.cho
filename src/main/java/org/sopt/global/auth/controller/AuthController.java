package org.sopt.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.sopt.global.auth.dto.RefreshTokenRequest;
import org.sopt.global.auth.dto.TokenResponse;
import org.sopt.global.auth.service.AuthService;
import org.sopt.global.auth.service.JwtService;
import org.sopt.global.dto.ApiResponse;
import org.sopt.member.dto.MemberResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    private static final long REFRESH_TOKEN_EXPIRES_IN_SECONDS = 1209600;

    @Operation(summary = "일반 로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        MemberResponse member = authService.loginWithCredentials(email, password);

        String accessToken = jwtService.generateAccessToken(member.userId(), member.email());
        String refreshToken = jwtService.generateRefreshToken(member.userId());

        authService.saveRefreshToken(member.userId(), refreshToken, REFRESH_TOKEN_EXPIRES_IN_SECONDS);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken);

        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "구글 로그인", description = "구글 OAuth 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<String>> loginWithGoogle(
            @RequestParam("code") String authorizationCode
    ) {
        MemberResponse member = authService.loginWithGoogle(authorizationCode);

        String token = jwtService.generateToken(member.userId(), member.email());

        return ResponseEntity.ok(ApiResponse.ok(token));
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        TokenResponse tokenResponse = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "로그아웃", description = "로그아웃하고 Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        MemberResponse member = authService.authenticateWithJwt(authorization);
        authService.logout(member.userId());

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "내 정보 조회", description = "JWT 토큰으로 인증하여 내 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        MemberResponse member = authService.authenticateWithJwt(authorization);

        return ResponseEntity.ok(ApiResponse.ok(member));
    }
}




