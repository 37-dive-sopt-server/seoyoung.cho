package org.sopt.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfoResponse(
        @JsonProperty("sub")
        String sub,  // 구글 고유 ID

        @JsonProperty("name")
        String name,

        @JsonProperty("email")
        String email,

        @JsonProperty("email_verified")
        Boolean emailVerified,

        @JsonProperty("picture")
        String picture
) {
}
