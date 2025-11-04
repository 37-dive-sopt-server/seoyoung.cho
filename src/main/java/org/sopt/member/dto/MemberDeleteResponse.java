package org.sopt.member.dto;

public record MemberDeleteResponse(Long deletedUserId
) {
    public static MemberDeleteResponse of(Long userId) {
        return new MemberDeleteResponse(userId);
    }
}
