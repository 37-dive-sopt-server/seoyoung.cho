package org.sopt.global.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh Token을 입력해주세요.")
        String refreshToken
) {
}
