package org.sopt.global.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.dto.ApiResponse;
import org.sopt.global.service.JwtService;
import org.sopt.member.dto.MemberResponse;
import org.sopt.global.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  @Operation(summary = "헤더 기반 Basic-Authentication")
  @PostMapping("/v1/login")
  public ResponseEntity<ApiResponse<MemberResponse>> login(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
  ) {
    log.info(authorization);
    MemberResponse result = authService.login(authorization);
    return ResponseEntity.ok(ApiResponse.ok(result));
  }

  @Operation(summary = "이메일/비밀번호 기반 로그인 (쿠키 발급)")
  @PostMapping("/v2/login")
  public ResponseEntity<ApiResponse<MemberResponse>> loginV2(
      @RequestParam("email") String email,
      @RequestParam("password") String password
  ) {
    MemberResponse result = authService.loginWithCredentials(email, password);

    String credentials = email + ":" + password;

    ResponseCookie cookie = ResponseCookie.from("basic", credentials)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .maxAge(Duration.ofHours(1))
        .path("/")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.ok(result));
  }

  @Operation(summary = "쿠키로 로그인 여부 확인")
  @GetMapping("/v2/me")
  public ResponseEntity<ApiResponse<MemberResponse>> checkSession(
      @CookieValue(value = "basic", required = false) String basicCookie
  ) {
    if (basicCookie == null) {
      throw new IllegalArgumentException("로그인 쿠키가 없습니다.");
    }

    int idx = basicCookie.indexOf(":");
    if (idx < 0) {
      throw new IllegalArgumentException("쿠키에 이메일과 비밀번호 구분자(:)가 없습니다.");
    }
    String email = basicCookie.substring(0, idx);
    String password = basicCookie.substring(idx + 1);

    log.info("[v2] 세션 확인, 이메일: {}", email);
    MemberResponse result = authService.loginWithCredentials(email, password);
    return ResponseEntity.ok(ApiResponse.ok(result));
  }

  @Operation(summary = "세션 기반 로그인 (JSESSIONID 발급)")
  @PostMapping("/v3/login")
  public ResponseEntity<ApiResponse<MemberResponse>> loginV3(
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      HttpSession session
  ) {
    MemberResponse result = authService.loginWithCredentials(email, password);
    session.setAttribute("LOGIN_MEMBER_ID", result.userId());
    return ResponseEntity.ok(ApiResponse.ok(result));
  }

  @Operation(summary = "세션 로그인 여부 확인")
  @GetMapping("/v3/me")
  public ResponseEntity<ApiResponse<MemberResponse>> checkSessionV3(
      HttpSession session
  ) {
    Long memberId = (Long) session.getAttribute("LOGIN_MEMBER_ID");
    MemberResponse result = authService.getMemberById(memberId);
    return ResponseEntity.ok(ApiResponse.ok(result));
  }

  @Operation(summary = "JWT 기반 로그인 (토큰 반환)")
  @PostMapping("/v4/login")
  public ResponseEntity<ApiResponse<String>> loginV4(
      @RequestParam("email") String email,
      @RequestParam("password") String password
  ) {
    MemberResponse member = authService.loginWithCredentials(email, password);
    String token = jwtService.generateToken(member.userId(), member.email());
    return ResponseEntity.ok(ApiResponse.ok(token));
  }

  @Operation(summary = "JWT 검증 (Authorization: Bearer)")
  @GetMapping("/v4/me")
  public ResponseEntity<ApiResponse<MemberResponse>> checkSessionV4(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
  ) {
    String raw = null;
    if (authorization != null && authorization.startsWith("Bearer ")) {
      raw = authorization.substring("Bearer ".length()).trim();
    }

    Long memberId = jwtService.verifyAndGetMemberId(raw);
    MemberResponse member = authService.getMemberById(memberId);
    return ResponseEntity.ok(ApiResponse.ok(member));
  }
}




