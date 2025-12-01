package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class InvalidFormatException extends BusinessException {
    public InvalidFormatException(String message) {
        super(ErrorCode.INVALID_ARGUMENT);
    }
}