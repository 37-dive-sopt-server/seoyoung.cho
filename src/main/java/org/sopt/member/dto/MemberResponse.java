package org.sopt.member.dto;

import org.sopt.member.domain.Gender;
import org.sopt.member.domain.Member;

import java.time.LocalDate;

public record MemberResponse(
        Long userId,
        String name,
        LocalDate birthdate,
        String email,
        Gender gender
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getBirthdate(),
                member.getEmail(),
                member.getGender()
        );
    }
}
