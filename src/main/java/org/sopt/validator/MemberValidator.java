package org.sopt.validator;

import org.sopt.domain.Member;
import org.sopt.exception.DuplicateMemberException;
import org.sopt.exception.MemberAgeException;
import org.sopt.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class MemberValidator {
    private static final int MINIMUM_AGE_FOR_REGISTRATION = 20;

    private final MemberRepository memberRepository;

    public MemberValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateNewMember(Member member) {
        validateAge(member);
        validateDuplicateMember(member);
    }

    private void validateAge(Member member) {
        if (member.getAge() < MINIMUM_AGE_FOR_REGISTRATION) {
            throw new MemberAgeException("만 " + MINIMUM_AGE_FOR_REGISTRATION + "세 미만은 회원으로 가입할 수 없습니다.");
        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
                });
    }
}
