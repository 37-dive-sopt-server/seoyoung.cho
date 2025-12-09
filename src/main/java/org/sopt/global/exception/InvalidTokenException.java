package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
