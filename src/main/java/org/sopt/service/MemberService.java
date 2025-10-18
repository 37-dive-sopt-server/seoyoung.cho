package org.sopt.service;

import org.sopt.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Long join(Member member);
    Optional<Member> findOne(Long memberId);
    List<Member> findAllMembers();
    boolean deleteMember(Long memberId);
}
