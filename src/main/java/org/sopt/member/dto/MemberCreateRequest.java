package org.sopt.member.dto;

import org.sopt.member.domain.Gender;

import java.time.LocalDate;

public record MemberCreateRequest(
        String name,
        LocalDate birthdate,
        String email,
        Gender gender
) {
}
