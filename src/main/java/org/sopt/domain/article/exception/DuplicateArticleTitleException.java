package org.sopt.domain.article.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BusinessException;

public class DuplicateArticleTitleException extends BusinessException {
    public DuplicateArticleTitleException() {
        super(ErrorCode.DUPLICATE_ARTICLE_TITLE);
    }
}