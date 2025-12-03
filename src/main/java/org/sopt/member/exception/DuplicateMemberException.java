package org.sopt.member.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {
    public DuplicateMemberException(String message) {
        super(ErrorCode.DUPLICATE_MEMBER);
    }

    public DuplicateMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}