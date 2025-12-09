package org.sopt.domain.comment.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class CommentForbiddenException extends BusinessException {
    public CommentForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
