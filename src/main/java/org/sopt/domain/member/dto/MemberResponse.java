package org.sopt.domain.member.dto;

import org.sopt.domain.member.domain.Gender;
import org.sopt.domain.member.domain.Member;

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
    public static MemberResponse of(Long id, String name, LocalDate birthDate, String email, Gender gender) {
        return new MemberResponse(id, name, birthDate,email, gender);
    }
}
