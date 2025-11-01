package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.dto.MemberCreateRequest;
import org.sopt.exception.EntityNotFoundException;
import org.sopt.repository.MemberRepository;
import org.sopt.service.validator.MemberValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    // 생성자 파라미터에서 @Qualifier로 fileRepo를 명시
    public MemberServiceImpl(@Qualifier("fileRepo") MemberRepository memberRepository, MemberValidator memberValidator) {
        this.memberRepository = memberRepository;
        this.memberValidator = memberValidator;
    }

    @Override
    public Member join(MemberCreateRequest request) {
        Member member = new Member(
                request.name(),
                request.birthdate(),
                request.email(),
                request.gender()
        );

        memberValidator.validateNewMember(member); // 검증 로직 Validator 위임
        return memberRepository.save(member);
    }

    @Override
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다."));
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = this.findOne(memberId);
        memberRepository.deleteById(member.getId());
    }
}
