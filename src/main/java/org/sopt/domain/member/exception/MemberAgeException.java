package org.sopt.domain.member.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class MemberAgeException extends BusinessException {
    public MemberAgeException() {
        super(ErrorCode.MEMBER_AGE_INVALID);
    }
}