package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.repository.MemberRepository;
import org.sopt.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private static long sequence = 1L;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long join(Member member) {
        validateDuplicateMember(member);
        validateAge(member);
        member.setId(sequence++);
        memberRepository.save(member);
        return member.getId();
    }

    // 나이 검증 로직
    private void validateAge(Member member) {
        if (member.getAge() < 20) {
            throw new IllegalStateException("만 20세 미만은 회원으로 가입할 수 없습니다.");
        }
    }

    // 이메일 중복 검사
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 이메일입니다.");
                });
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
            return true; // 삭제 성공
        }
        return false;
    }
}
