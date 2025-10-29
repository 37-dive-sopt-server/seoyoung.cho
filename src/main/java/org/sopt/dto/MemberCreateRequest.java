package org.sopt.dto;

import org.sopt.domain.Gender;

import java.time.LocalDate;

public record MemberCreateRequest(
        String name,
        LocalDate birthdate,
        String email,
        Gender gender
) {
}
