package org.sopt.service.validator;

import org.sopt.domain.Member;
import org.sopt.exception.DuplicateMemberException;
import org.sopt.exception.InvalidEmailFormatException;
import org.sopt.exception.MemberAgeException;
import org.sopt.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class MemberValidator {
    private static final int MINIMUM_AGE_FOR_REGISTRATION = 20;
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    );

    private final MemberRepository memberRepository;

    public MemberValidator(@Qualifier("fileRepo") MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateNewMember(Member member) {
        validateRequiredFields(member);
        validateEmailFormat(member.getEmail());
        validateAge(member);
        validateDuplicateMember(member);
    }

    private void validateRequiredFields(Member member) {
        if (member.getName() == null || member.getName().isBlank()) {
            throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");
        }
        if (member.getBirthdate() == null) {
            throw new IllegalArgumentException("생년월일은 필수 입력 항목입니다.");
        }
        if (member.getEmail() == null || member.getEmail().isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력 항목입니다.");
        }
        if (member.getGender() == null) {
            throw new IllegalArgumentException("성별은 필수 입력 항목입니다.");
        }
    }

    private void validateEmailFormat(String email) {
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidEmailFormatException("이메일 형식이 올바르지 않습니다.");
        }
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
