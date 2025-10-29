package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.repository.MemberRepository;
import org.sopt.validator.MemberValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    public MemberServiceImpl(MemberRepository memberRepository, MemberValidator memberValidator) {
        this.memberRepository = memberRepository;
        this.memberValidator = memberValidator;
    }

    @Override
    public Long join(Member member) {
        memberValidator.validateNewMember(member); // 검증 로직 Validator 위임
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Override
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public boolean deleteMember(Long memberId) {
        if (memberRepository.findById(memberId).isPresent()) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }
}
