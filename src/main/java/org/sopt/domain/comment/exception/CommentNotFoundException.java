package org.sopt.domain.comment.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    public CommentNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}
