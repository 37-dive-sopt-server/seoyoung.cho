package org.sopt.global.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.global.auth.dto.GoogleTokenResponse;
import org.sopt.global.auth.dto.GoogleUserInfoResponse;
import org.sopt.global.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {
    private final RestClient restClient = RestClient.create();

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    public GoogleTokenResponse getAccessToken(String authorizationCode) {
        log.info("구글 Access Token 요청 - code: {}", authorizationCode);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        try {
            GoogleTokenResponse response = restClient.post()
                    .uri(GOOGLE_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(params)
                    .retrieve()
                    .body(GoogleTokenResponse.class);

            log.info("구글 Access Token 발급 성공");
            return response;

        } catch (Exception e) {
            log.error("구글 토큰 발급 실패: {}", e.getMessage());
            throw new UnauthorizedException("구글 토큰 발급에 실패했습니다.");
        }
    }

    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        log.info("구글 사용자 정보 조회");

        try {
            GoogleUserInfoResponse response = restClient.get()
                    .uri(GOOGLE_USERINFO_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(GoogleUserInfoResponse.class);

            log.info("구글 사용자 정보 조회 성공 - email: {}", response.email());
            return response;

        } catch (Exception e) {
            log.error("구글 사용자 정보 조회 실패: {}", e.getMessage());
            throw new UnauthorizedException("구글 사용자 정보 조회에 실패했습니다.");
        }
    }
}
