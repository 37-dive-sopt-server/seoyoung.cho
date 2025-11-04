package org.sopt.member.service;

import org.sopt.member.domain.Member;
import org.sopt.member.dto.MemberCreateRequest;

import java.util.List;

public interface MemberService {
    Member join(MemberCreateRequest request);
    Member findOne(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
}
