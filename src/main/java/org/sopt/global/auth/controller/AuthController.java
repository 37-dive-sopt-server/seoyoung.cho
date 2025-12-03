package org.sopt.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.global.auth.dto.GoogleLoginRequest;
import org.sopt.global.auth.dto.LoginRequest;
import org.sopt.global.auth.dto.RefreshTokenRequest;
import org.sopt.global.auth.dto.TokenResponse;
import org.sopt.global.auth.service.AuthService;
import org.sopt.global.dto.ApiResponse;
import org.sopt.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "일반 로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request.email(), request.password());
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "구글 로그인", description = "구글 OAuth 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<TokenResponse>> loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        TokenResponse tokenResponse = authService.loginWithGoogle(request.authorizationCode());
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "로그아웃", description = "로그아웃하고 Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "내 정보 조회", description = "JWT 토큰으로 인증하여 내 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(@AuthenticationPrincipal Long memberId) {
        MemberResponse memberResponse = authService.getMemberById(memberId);
        return ResponseEntity.ok(ApiResponse.ok(memberResponse));
    }
}




