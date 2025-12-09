package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
