package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED);
    }
}

