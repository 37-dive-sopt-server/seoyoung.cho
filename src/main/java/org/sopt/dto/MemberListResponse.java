package org.sopt.dto;

import org.sopt.domain.Member;

import java.util.List;

public record MemberListResponse(List<MemberResponse> members
) {
    public static MemberListResponse from(List<Member> memberList) {
        List<MemberResponse> memberDtos = memberList.stream()
                .map(MemberResponse::from)
                .toList();
        return new MemberListResponse(memberDtos);
    }
}
