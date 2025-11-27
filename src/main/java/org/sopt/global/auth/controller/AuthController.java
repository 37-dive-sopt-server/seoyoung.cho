package org.sopt.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        MemberResponse member = authService.loginWithCredentials(email, password);
        String token = jwtService.generateToken(member.userId(), member.email());

        return ResponseEntity.ok(ApiResponse.ok(token));
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




