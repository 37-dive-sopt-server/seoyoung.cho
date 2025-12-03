package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class DataStorageException extends BusinessException {
    public DataStorageException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
