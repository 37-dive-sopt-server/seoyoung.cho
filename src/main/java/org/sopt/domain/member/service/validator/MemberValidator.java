package org.sopt.domain.member.service.validator;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.exception.DuplicateMemberException;
import org.sopt.domain.member.exception.MemberAgeException;
import org.sopt.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private static final int MINIMUM_AGE_FOR_REGISTRATION = 20;

    private final MemberRepository memberRepository;

    public void validateNewMember(Member member) {
        validateAge(member);
        validateDuplicateMember(member);
    }

    private void validateAge(Member member) {
        if (member.getAge() < MINIMUM_AGE_FOR_REGISTRATION) {
            throw new MemberAgeException();
        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new DuplicateMemberException();
                });
    }
}
