package org.sopt.member.service;

import org.sopt.member.domain.Member;
import org.sopt.member.dto.MemberCreateRequest;
import org.sopt.member.exception.EntityNotFoundException;
import org.sopt.member.repository.MemberRepository;
import org.sopt.member.service.validator.MemberValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public MemberServiceImpl(MemberRepository memberRepository, MemberValidator memberValidator) {
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
