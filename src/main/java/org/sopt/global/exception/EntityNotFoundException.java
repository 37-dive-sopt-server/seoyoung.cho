package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}
