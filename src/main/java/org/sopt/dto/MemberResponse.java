package org.sopt.dto;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String name,
        LocalDate birthdate,
        String email,
        Gender gender
) {
    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getBirthdate(),
                member.getEmail(),
                member.getGender()
        );
    }
}
