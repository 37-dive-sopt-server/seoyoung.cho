package org.sopt.service;

import org.sopt.domain.Member;
import org.sopt.dto.MemberCreateRequest;

import java.util.List;

public interface MemberService {
    Member join(MemberCreateRequest request);
    Member findOne(Long memberId);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
}
