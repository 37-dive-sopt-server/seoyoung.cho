package org.sopt.global.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "Authorization Code는 필수입니다.")
        String authorizationCode
) {
}
