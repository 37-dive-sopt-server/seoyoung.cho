package org.sopt.domain.member.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {
    public DuplicateMemberException() {
        super(ErrorCode.DUPLICATE_MEMBER);
    }
}