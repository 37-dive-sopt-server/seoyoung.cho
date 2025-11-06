package org.sopt.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.global.deserializer.GenderDeserializer;
import org.sopt.member.domain.Gender;

import java.time.LocalDate;

public record MemberCreateRequest(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        String name,

        @NotNull(message = "생년월일은 필수 입력 항목입니다.")
        LocalDate birthdate,

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotNull(message = "성별은 필수 입력 항목입니다.")
        @JsonDeserialize(using = GenderDeserializer.class)
        Gender gender
) {
}
