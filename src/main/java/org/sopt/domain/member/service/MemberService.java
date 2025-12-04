package org.sopt.domain.member.service;

import org.sopt.domain.member.domain.Member;
import org.sopt.domain.member.dto.MemberCreateRequest;

import java.util.List;

public interface MemberService {
    Member join(MemberCreateRequest request);
    Member findOne(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
}
