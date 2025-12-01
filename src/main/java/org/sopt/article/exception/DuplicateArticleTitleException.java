package org.sopt.article.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class DuplicateArticleTitleException extends BusinessException {
    public DuplicateArticleTitleException(String message) {
        super(ErrorCode.DUPLICATE_ARTICLE_TITLE);
    }

    public DuplicateArticleTitleException(ErrorCode errorCode) {
        super(errorCode);
    }
}