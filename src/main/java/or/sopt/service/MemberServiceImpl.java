package or.sopt.service;

import or.sopt.domain.Member;
import or.sopt.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    private static long sequence = 1L;

    public Long join(String name) {

        Member member = new Member(sequence++, name);
        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
